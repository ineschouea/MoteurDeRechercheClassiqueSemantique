/**
 *  @(#)UserLinkAdditionFinder.java 0.02 05/11/2017
 *  Copyright (C) 2015-2017 MER-C
 *
 *  This program is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU General Public License
 *  as published by the Free Software Foundation; either version 3
 *  of the License, or (at your option) any later version. Additionally
 *  this file is subject to the "Classpath" exception.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software Foundation,
 *  Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA.
 */
package org.wikipedia.tools;

import java.io.*;
import java.util.*;
import java.util.regex.*;
import java.nio.charset.Charset;
import java.nio.file.*;
import java.time.OffsetDateTime;
import java.util.stream.Collectors;
import javax.swing.JFileChooser;
import org.wikipedia.*;

/**
 *  Finds links added by a user in the main namespace.
 *  @author MER-C
 *  @version 0.02
 */
public class UserLinkAdditionFinder
{
    private static int threshold = 50;
    private final WMFWiki wiki;
    
    /**
     *  Runs this program.
     *  @param args the command line arguments (see code for documentation)
     *  @throws IOException if a network error occurs
     */
    public static void main(String[] args) throws IOException
    {
        // parse command line args
        Map<String, String> parsedargs = new CommandLineParser()
            .synopsis("org.wikipedia.tools.UserLinkAdditionFinder", "[options] [file]")
            .description("Finds the set of links added by a list of users.")
            .addHelp()
            .addVersion("UserLinkAdditionFinder v0.02\n" + CommandLineParser.GPL_VERSION_STRING)
            .addSingleArgumentFlag("--wiki", "example.org", "The wiki to fetch data from (default: en.wikipedia.org)")
            .addSingleArgumentFlag("--user", "user", "Get links for this user only.")
            .addBooleanFlag("--linksearch", "Conduct a linksearch to count links and filter commonly used domains.")
            .addBooleanFlag("--removeblacklisted", "Remove blacklisted links")
            .addSingleArgumentFlag("--fetchafter", "date", "Fetch only edits after this date.")
            .addSection("If a file is not specified, a dialog box will prompt for one.")
            .parse(args);

        WMFWiki thiswiki = WMFWiki.newSession(parsedargs.getOrDefault("--wiki", "en.wikipedia.org"));
        boolean linksearch = parsedargs.containsKey("--linksearch");
        boolean removeblacklisted = parsedargs.containsKey("--removeblacklisted");
        String user = parsedargs.get("--user");
        String datestring = parsedargs.get("--fetchafter");
        String filename = parsedargs.get("default");
        OffsetDateTime date = datestring == null ? null : OffsetDateTime.parse(datestring);
        
        UserLinkAdditionFinder finder = new UserLinkAdditionFinder(thiswiki);
        ExternalLinkPopularity elp = new ExternalLinkPopularity(thiswiki);
        elp.setMaxLinks(threshold);

        // read in from file
        List<String> users;
        if (user == null)
        {
            Path fp = null;
            if (filename == null)
            {
                JFileChooser fc = new JFileChooser();
                if (fc.showOpenDialog(null) != JFileChooser.APPROVE_OPTION)
                    System.exit(0);
                fp = fc.getSelectedFile().toPath();
            }
            else
                fp = Paths.get(filename);
            users = Files.readAllLines(fp, Charset.forName("UTF-8"));
        }
        else
            users = Arrays.asList(user);

        // Map structure:
        // * results: revid -> links added in that revision
        // * linkdomains: link -> domain
        // * linkcounts: domain -> count of links
        // * stillthere: page name -> link -> whether it is still there
        Map<Wiki.Revision, List<String>> results = finder.getLinksAdded(users, date);
        if (results.isEmpty())
        {
            System.out.println("No links found.");
            System.exit(0);
        }
        
        Map<String, String> linkdomains = new HashMap<>();
        for (Map.Entry<Wiki.Revision, List<String>> entry : results.entrySet())
        {
            for (String link : entry.getValue())
            {
                String domain = ExternalLinks.extractDomain(link);
                if (domain != null) // must be parseable
                    linkdomains.put(link, domain);
            }
        }
        
        // remove blacklisted links
        Collection<String> domains = linkdomains.values();
        if (removeblacklisted)
        {
            Iterator<String> iter = domains.iterator();
            while (iter.hasNext())
            {
                String link = iter.next();
                if (thiswiki.isSpamBlacklisted(linkdomains.get(link)))
                    iter.remove();
            }
        }
        
        // remove commonly used domains
        Map<String, Integer> linkcounts = null;
        if (linksearch)
        {
            linkcounts = elp.determineLinkPopularity(domains);
            Iterator<Map.Entry<String, Integer>> iter = linkcounts.entrySet().iterator();
            while (iter.hasNext())
            {
                Map.Entry<String, Integer> entry = iter.next();
                if (entry.getValue() >= threshold)
                {
                    iter.remove();
                    domains.remove(entry.getKey());
                }
            }
        }
        Map<String, Map<String, Boolean>> stillthere = finder.checkIfLinksAreStillPresent(results);
        
        // output results
        System.out.println(finder.outputWikitableResults(results, linkcounts, stillthere));
        System.out.println("== Domain list ==");
        System.out.println(Pages.toWikitextTemplateList(domains, "spamlink", false));
        System.out.println();
        System.out.println("== Blacklist log ==");
        System.out.println(finder.generateBlacklistLog(results, linkdomains));
        System.out.flush();
    }
    
    /**
     *  Creates a new instance of this tool.
     *  @param wiki the wiki to fetch data from
     */
    public UserLinkAdditionFinder(WMFWiki wiki)
    {
        this.wiki = wiki;
    }
    
    /**
     *  Returns the wiki that this tool fetches data from.
     *  @return (see above)
     */
    public Wiki getWiki()
    {
        return wiki;
    }

    /**
     *  Fetches the list of links added by a list of users. The list of users
     *  must be a list of usernames only, no User: prefix or wikilinks allowed.
     *  @param users the list of users to get link additions for
     *  @param earliest return edits no earlier than this date
     *  @return a Map: revision &#8594; added links
     *  @throws IOException if a network error occurs
     */
    public Map<Wiki.Revision, List<String>> getLinksAdded(List<String> users, OffsetDateTime earliest) throws IOException
    {
        Wiki.RequestHelper rh = wiki.new RequestHelper()
            .inNamespaces(Wiki.MAIN_NAMESPACE)
            .withinDateRange(earliest, null);
        Map<Wiki.Revision, List<String>> results = new HashMap<>();
        List<List<Wiki.Revision>> contribs = wiki.contribs(users, null, rh);
        List<Wiki.Revision> revisions = contribs.stream()
            .flatMap(List::stream)
            .filter(revision -> !revision.isContentDeleted())
            .collect(Collectors.toList());
        for (Wiki.Revision revision : revisions)
        {
            // remove all sets { revision, links... } where no links are added
            List<String> temp = parseDiff(revision);
            if (!temp.isEmpty())
                results.put(revision, temp);
        }
        return results;
    }
    
    /**
     *  For a map that contains revision data &#8594; links added in that 
     *  revision, check whether the links still exist in the current version of
     *  the article. Such a map can be obtained by calling {@link 
     *  #getLinksAdded(List, OffsetDateTime)}.
     *  @param data a map containing revision data &#8594; links added in that 
     *  revision
     *  @return a map containing page &#8594; link &#8594; whether it is still 
     *  there
     *  @throws IOException if a network error occurs
     */
    public Map<String, Map<String, Boolean>> checkIfLinksAreStillPresent(Map<Wiki.Revision, List<String>> data) throws IOException
    {
        Map<String, List<String>> resultsbypage = new HashMap<>();
        data.forEach((revision, listoflinks) ->
        {
            String page = revision.getTitle();
            List<String> list = resultsbypage.get(page);
            if (list == null)
            {
                list = new ArrayList<>();
                resultsbypage.put(page, list);
            }
            list.addAll(listoflinks);
        });
        return Pages.of(wiki).containExternalLinks(resultsbypage);
    }
    
    public String outputWikitableResults(Map<Wiki.Revision, List<String>> data, 
        Map<String, Integer> linkcounts, Map<String, Map<String, Boolean>> stillthere)
    {
        StringBuilder builder = new StringBuilder(100000);
        builder.append("{| class=\"wikitable\"\n");
        for (Map.Entry<Wiki.Revision, List<String>> entry : data.entrySet())
        {
            Wiki.Revision revision = entry.getKey();
            List<String> links = entry.getValue();
            if (links.isEmpty())
                continue;
            Map<String, Boolean> revlinkexists = stillthere.get(revision.getTitle());
            builder.append("|-\n|| [[Special:Diff/");
            builder.append(revision.getID());
            builder.append("]]\n||\n");
            for (int i = 0; i < links.size(); i++)
            {
                String link = links.get(i);
                builder.append("* ");
                builder.append(link);
                boolean remaining = revlinkexists.get(link);
                builder.append(remaining ? " ('''STILL THERE'''" : " (removed");
                if (linkcounts != null)
                {
                    String domain = ExternalLinks.extractDomain(link);
                    builder.append("; ");
                    builder.append(linkcounts.get(domain));
                    builder.append(" links: [[Special:Linksearch/*.");
                    builder.append(domain);
                    builder.append("|http]], [[Special:Linksearch/https://*.");
                    builder.append(domain);
                    builder.append("|https]])");
                }
                else
                    builder.append(")");
                builder.append("\n");
            }
        }
        builder.append("|}");
        return builder.toString();
    }
    
    public String generateBlacklistLog(Map<Wiki.Revision, List<String>> data, Map<String, String> domains)
    {
        // transform to domain -> spammers
        Map<String, List<String>> spammers = new HashMap<>();
        data.forEach((revision, listoflinks) ->
        {
            String user = revision.getUser();
            for (String link : listoflinks)
            {
                String domain = domains.get(link);
                List<String> users = spammers.getOrDefault(domain, new ArrayList<>());
                users.add(user);
                spammers.putIfAbsent(domain, users);
            }
        });
        
        // generate output
        StringBuilder sb = new StringBuilder(100000);
        spammers.forEach((key, value) ->
        {
            String domain = key.replace(".", "\\.");
            sb.append(" \\b");
            sb.append(domain);
            sb.append("\\b");
            for (int i = domain.length(); i < 35; i++)
                sb.append(' ');
            sb.append(" # ");
            for (String spammer : value)
            {
                sb.append("{{user|");
                sb.append(spammer);
                sb.append("}} ");
            }
            sb.append("\n");
        });
        return sb.toString();
    }

    /**
     *  Returns a list of external links added by a particular revision.
     *  @param revision the revision to check of added external links.
     *  @return the list of added URLs
     *  @throws IOException if a network error occurs
     */
    public List<String> parseDiff(Wiki.Revision revision) throws IOException
    {
        // fetch the diff
        String diff = revision.isNew() ? revision.getText() : revision.diff(Wiki.PREVIOUS_REVISION);
        // filter dummy edits
        if (diff == null || diff.isEmpty())
            return Collections.emptyList();
        List<String> links = new ArrayList<>();

        // some HTML strings we are looking for
        // see https://en.wikipedia.org/w/api.php?action=compare&fromrev=77350972&torelative=prev
        String diffaddedbegin = "<td class=\"diff-addedline\">";
        String diffaddedend = "</td>";
        String deltabegin = "<ins class=\"diffchange diffchange-inline\">";
        String deltaend = "</ins>";
        // link regex
        Pattern pattern = Pattern.compile("https?://.+?\\..{2,}?(?:\\s|]|<|$)");

        // Condense deltas to avoid problems like https://en.wikipedia.org/w/index.php?title=&diff=prev&oldid=486611734
        diff = diff.toLowerCase(wiki.locale());
        diff = diff.replace(deltaend + " " + deltabegin, " ");
        diff = diff.replace("&lt;", "<");
        for (int j = diff.indexOf(diffaddedbegin); j >= 0; j = diff.indexOf(diffaddedbegin, j))
        {
            int y2 = diff.indexOf(diffaddedend, j);
            String addedline = diff.substring(j + diffaddedbegin.length(), y2);
            addedline = addedline.replaceFirst("^<div>", "");
            addedline = addedline.replace("</div>", "");
            if (addedline.contains(deltabegin))
            {
                for (int k = addedline.indexOf(deltabegin); k >= 0; k = addedline.indexOf(deltabegin, k))
                {
                    int y3 = addedline.indexOf(deltaend, k);
                    String delta = addedline.substring(k + deltabegin.length(), y3);
                    // extract links
                    Matcher matcher = pattern.matcher(delta);
                    while (matcher.find())
                        links.add(matcher.group().split("[\\|<\\]\\s\\}]")[0]);
                    k = y3;
                }
            }
            else
            {
                Matcher matcher = pattern.matcher(addedline);
                while (matcher.find())
                    links.add(matcher.group().split("[\\|<\\]\\s\\}]")[0]);
            }
            j = y2;
        }
        return links;
    }
}
