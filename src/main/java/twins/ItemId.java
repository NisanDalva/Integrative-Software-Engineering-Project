package twins;

public class ItemId {
	private String space;
	private String id;
	
	
	public ItemId() {
	}


	public ItemId(String space, String id) {
		super();
		this.space = space;
		this.id = id;
	}


	public String getSpace() {
		return space;
	}


	public void setSpace(String space) {
		this.space = space;
	}


	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}


	@Override
	public String toString() {
		return "ItemId [space=" + space + ", id=" + id + "]";
	}
	
	
}
