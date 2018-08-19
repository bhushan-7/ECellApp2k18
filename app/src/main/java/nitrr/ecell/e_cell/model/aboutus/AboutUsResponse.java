package nitrr.ecell.e_cell.model.aboutus;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

import nitrr.ecell.e_cell.model.auth.GenericResponse;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AboutUsResponse extends GenericResponse implements Serializable {

    @SerializedName("Faculty")
    @Expose
    private List<TeamDetails> faculty;


    @SerializedName("Student")
    @Expose
    private List<TeamDetails> student;

    public List<TeamDetails> getFaculty() {
        return faculty;
    }

    public List<TeamDetails> getStudent() {
        return student;
    }
}