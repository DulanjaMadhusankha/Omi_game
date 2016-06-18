
/**
 *
 * @author dulanja
 */
/*In here maintain the 3 states which are loding , Waiting and Playing states.*/
public enum Status {
    Loading("loading Cards"),
    Waiting("Waiting for others connect"),
    Play("play Your hand");
    
    private final String status;
    
    Status(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
