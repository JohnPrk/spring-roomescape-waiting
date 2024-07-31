package roomescape.domain.member.domain;


import jakarta.persistence.*;
import roomescape.domain.reservation.domain.Reservation;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String email;
    private String password;
    private String role;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Reservation> reservations = new ArrayList<>();

    public Member(Long id, String name, String email, String password, String role) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public Member() {
    }

    public boolean isAdmin() {
        return !this.role.equals(Role.ADMIN.getRole());
    }

    public void grantAdminRole() {
        this.role = Role.ADMIN.getRole();
    }

    public void connectWith(Reservation reservation) {
        reservations.add(reservation);
        reservation.connectWith(this);
    }

    public boolean authenticationCheck(Long memberId) {
        return Objects.equals(this.id, memberId);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getRole() {
        return role;
    }
}
