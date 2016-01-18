package log;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;
import model.communication.protocol.EntityTarget;

@Entity
@Table(name = "LOG_RECORDS")
public class LogRecord implements Serializable {

    private Long id;
    private Date time;
    private Long targetId;
    private EntityTarget targetEntity;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", precision = 18)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "target_entity", nullable = false, length = 16)
    public EntityTarget getTargetEntity() {
        return targetEntity;
    }

    public void setTargetEntity(EntityTarget targetEntity) {
        this.targetEntity = targetEntity;
    }

    @Column(name = "target_id", precision = 18, nullable = false)
    public Long getTargetId() {
        return targetId;
    }

    public void setTargetId(Long targetId) {
        this.targetId = targetId;
    }

    @Temporal(javax.persistence.TemporalType.DATE)
    @Column(name = "event_time", nullable = false)
    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

}
