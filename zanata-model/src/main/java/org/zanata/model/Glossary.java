package org.zanata.model;

import java.io.Serializable;
import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;

import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author Alex Eng <a href="mailto:aeng@redhat.com">aeng@redhat.com</a>
 */
@Entity
@NoArgsConstructor
@Access(AccessType.FIELD)
@Getter
@Indexed
public class Glossary implements Serializable {
    public Glossary(String qualifiedName) {
        this.qualifiedName = qualifiedName;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Unique glossary name
     *
     * e.g. project/{project slug}, global/default
     */
    @NotNull
    @Field
    private String qualifiedName;
}
