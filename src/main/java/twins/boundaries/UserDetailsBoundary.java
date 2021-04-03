package twins.boundaries;

//{
//	"first":"Jane",
//	"last":"Janes"
//}
public class UserDetailsBoundary {
	private String first;
	private String last;

	public UserDetailsBoundary() {
	}

	public UserDetailsBoundary(String first, String last) {
		super();
		this.first = first;
		this.last = last;
	}

	public String getFirst() {
		return first;
	}

	public void setFirst(String first) {
		this.first = first;
	}

	public String getLast() {
		return last;
	}

	public void setLast(String last) {
		this.last = last;
	}

	@Override
	public String toString() {
		return "UserDetailsBoundary [first=" + first + ", last=" + last + "]";
	}

}
