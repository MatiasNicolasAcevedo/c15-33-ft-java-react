
package com.clonair.back.property;

import com.clonair.back.image.Image;
import com.clonair.back.location.Location;
import com.clonair.back.user.User;
import jakarta.persistence.*;
import java.util.List;
import lombok.Data;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.GenericGenerator;

@Data
@Entity
public class Property {
    
    @Id
    @GeneratedValue (generator= "uuid")
    @GenericGenerator (name= "uuid", strategy = "uuid2")
    @Column(name = "id", updatable = false, nullable = false)
    private String id;

    private String title;

    @Enumerated(EnumType.STRING)
    private Category category;

    @Enumerated(EnumType.STRING)
    private SubCategory subCategory;

    private String description;

    private double value;

    private boolean active;

    @ManyToOne
    @Cascade(value = CascadeType.ALL)
    private User user;

    @OneToOne
    @Cascade(value = CascadeType.ALL)
    private Location location;

    @OneToMany
    private List<Image> images;

    private List<String> availability;

}