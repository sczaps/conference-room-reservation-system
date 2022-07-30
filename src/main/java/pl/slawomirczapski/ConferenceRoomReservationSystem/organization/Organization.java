package pl.slawomirczapski.ConferenceRoomReservationSystem.organization;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Objects;


interface AddOrganization {
}

interface UpdateOrganization {
}

@Entity
public class Organization {

    @Id
    @Size(min = 2, max = 20, groups = {AddOrganization.class, UpdateOrganization.class})
    @NotBlank(groups = AddOrganization.class)
    private String name;
    private String description;

    public Organization() {
    }

    public Organization(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Organization that = (Organization) o;
        return Objects.equals(name, that.name) && Objects.equals(description, that.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description);
    }
}