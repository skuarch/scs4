package beans;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 *
 * @author skuarch
 */
@Entity
@Table(name="collectors")
public class Collector implements Serializable {
    
    @Id
    @Column(name="collector_id")
    private long id;
    
    @Column(name="collector_name")
    private String name;
    
    @Column(name="collector_ip")
    private String ip;
    
    @Column(name="collector_port")
    private int port;
    
    @Column(name="collector_description")
    private String description;
    
    @Column(name="collector_active")
    private int active;
    
    //==========================================================================
    public Collector(){
    
    }

    public int getActive() {
        return active;
    }

    public void setActive(int active) {
        this.active = active;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
    
    
    
} // end class
