package id.test.springboottesting.model;

import id.test.springboottesting.util.annotation.ValidPassword;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import org.hibernate.proxy.HibernateProxy;

import java.util.Objects;

/***
 * Project Name     : spring-boot-testing
 * Username         : Teten Nugraha
 * Date Time        : 12/18/2019
 * Telegram         : @tennugraha
 */

@Entity
@Table(name = "users")
@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Email(message = "Email is not valid")
    @NotEmpty(message = "Email should not be empty")
    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Column(nullable = false, length = 100)
    @ValidPassword
    private String password;

    @NotEmpty(message = "Username should not be empty")
    @Column(nullable = false, length = 100)
    private String name;

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        if (this.getClass() != o.getClass()) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy hibernateProxy ? (hibernateProxy).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy hibernateProxy ? (hibernateProxy).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        User user = (User) o;
        return getId() != null && Objects.equals(getId(), user.getId());
    }

    @Override
    public final int hashCode() {
        return getClass().hashCode();
    }
}
