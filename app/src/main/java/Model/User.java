package Model;

public class User {
    String username, email;
    Integer current_exams, total_exams;

    public User(){

    }

    public User(String username, String email, Integer current_exams, Integer total_exams) {
        this.username = username;
        this.email = email;
        this.current_exams = current_exams;
        this.total_exams = total_exams;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getCurrent_exams() {
        return current_exams;
    }

    public void setCurrent_exams(Integer current_exams) {
        this.current_exams = current_exams;
    }

    public Integer getTotal_exams() {
        return total_exams;
    }

    public void setTotal_exams(Integer total_exams) {
        this.total_exams = total_exams;
    }
}
