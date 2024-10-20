package dh.javaproject.chatservice.entities;

import dh.javaproject.chatservice.enums.Gender;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    Long id;

    String email;

    String nickname;

    String name;

    @Enumerated(EnumType.STRING)
    Gender gender;

    String phoneNumber;

    LocalDate birthday;

    String role;
}
