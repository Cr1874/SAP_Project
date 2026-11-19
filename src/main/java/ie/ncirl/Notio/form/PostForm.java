package ie.ncirl.Notio.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

// This class is used to validate the data submitted in the form for creating or editing a post
// The @NotBlank and @Size annotations ensure that the title and body fields are not empty and do not exceed the specified length limits.
public class PostForm {
    @NotBlank @Size (max=100)
    private String title;
    @NotBlank @Size (max=1000)
    private String body;

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getBody() {
        return body;
    }
    public void setBody(String body) {
        this.body = body;
    }
}
