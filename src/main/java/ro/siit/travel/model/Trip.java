package ro.siit.travel.model;
import org.springframework.format.annotation.DateTimeFormat;
import javax.persistence.*;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.Objects;

/**
 * Trip Class
 *
 * @author Georgiana Simpetreanu
 * @version 1.0
 * Date 10/21/2019.
 */
@Entity()
@Table(name = "trip")
public class Trip {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "trip_id", unique = true, nullable = false)
    private int id;

    @Column(name = "trip_name", nullable = false)
    private String name;

    @Size(min =2, max = 4000)
    @Column(name = "impression", nullable = false)
    private String impression;

    @Past
    @DateTimeFormat(pattern = "dd.MM.yyyy")
    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Past
    @DateTimeFormat(pattern = "dd.MM.yyyy")
    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @Column(name = "location", nullable = false)
    private String location;

    @Column(name="photo_1")
    private String photo1;

    @Column(name="photo_2")
    private String photo2;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName="user_id")
    private User userId;

    public Trip() {
    }

    public void setUser(User user) {
        this.userId = user;
        if (!user.getTrips().contains(this)) {
            user.getTrips().add(this);
        }
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImpression() {
        return impression;
    }

    public void setImpression(String impression) {
        this.impression = impression;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public User getUserId() {
        return userId;
    }

    public void setUserId(User userId) {
        this.userId = userId;
    }

    public String getPhoto1() {
        return photo1;
    }

    public void setPhoto1(String photo1) {
        this.photo1 = photo1;
    }

    public String getPhoto2() {
        return photo2;
    }

    public void setPhoto2(String photo2) {
        this.photo2 = photo2;}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Trip trip = (Trip) o;
        return id == trip.id &&
                Objects.equals(name, trip.name) &&
                Objects.equals(impression, trip.impression) &&
                Objects.equals(startDate, trip.startDate) &&
                Objects.equals(endDate, trip.endDate) &&
                Objects.equals(location, trip.location) &&
                Objects.equals(userId, trip.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, impression, startDate, endDate, location);
    }

    @Override
    public String toString() {
        return "Trip{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", impression='" + impression + '\'' +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", location='" + location + '\'' +
                ", userId=" + userId +
                '}';
    }
}
