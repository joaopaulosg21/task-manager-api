package projeto.api.taskmanager.task;

import java.time.LocalDate;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import projeto.api.taskmanager.user.User;

@Entity
@Table(name = "tasks")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Task {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "title não pode ser NULL")
    private String title;

    @NotBlank(message = "description não pode ser NULL")
    private String description;

    @CreatedDate
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate createdAt;

    @NotNull(message = "limit_date não pode ser NULL")
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate limit_date;

    @JoinColumn(referencedColumnName = "id",columnDefinition = "user_id")
    @ManyToOne()
    @JsonIgnoreProperties({"password"})
    private User user;

    private Status status;

    public Task(String title, String description,LocalDate createdAt, LocalDate limit_date) {
        this.title = title;
        this.description = description;
        this.limit_date = limit_date;
        this.createdAt = createdAt;
    }

        public Task(String title, String description,LocalDate limit_date) {
        this.title = title;
        this.description = description;
        this.limit_date = limit_date;
    }
}
