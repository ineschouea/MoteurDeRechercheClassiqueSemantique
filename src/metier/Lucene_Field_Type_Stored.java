package metier;

import org.apache.lucene.document.FieldType;

public class Lucene_Field_Type_Stored {
	
	public static final FieldType TYPE_STORED = new FieldType();
	static {
        TYPE_STORED.setIndexed(true);
        TYPE_STORED.setTokenized(true);
        TYPE_STORED.setStored(true);
        TYPE_STORED.setStoreTermVectors(true);
        TYPE_STORED.setStoreTermVectorPositions(true);
        TYPE_STORED.freeze();
    }


}
