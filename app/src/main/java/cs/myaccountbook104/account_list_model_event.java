package cs.myaccountbook104;

public class account_list_model_event {

    private int imageView;

    private String account_name;
    private String account_number;
    //private String deleteaccount;
    private int id;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public account_list_model_event() {
    }


    public account_list_model_event(int imageView, String account_name,String account_number,int id) {
        this.imageView = imageView;
        this.account_name = account_name;
        this.account_number = account_number;
        //this.account_time = account_time;
        this.id = id;
        //this.deleteaccount = deleteaccount;
    }

    public int getImageView() {
        return imageView;
    }

    public void setImageView(int imageView) {
        this.imageView = imageView;
    }

    /**   public String getAccount_time() {
        return account_time;
    } **/

    /** public void setAccount_time(String account_time) {
        this.account_time = account_time;
    } **/


    public String getAccount_name() {
        return account_name;
    }

    public void setAccount_name(String account_name) {
        this.account_name = account_name;
    }

    public String getAccount_number() {
        return account_number;
    }

    public void setAccount_number(String account_number) {
        this.account_number = account_number;
    }

}
