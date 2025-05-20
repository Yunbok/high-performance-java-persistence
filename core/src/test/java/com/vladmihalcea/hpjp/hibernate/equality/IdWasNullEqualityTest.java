package com.vladmihalcea.hpjp.hibernate.equality;

import com.vladmihalcea.hpjp.hibernate.identifier.Identifiable;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.junit.Test;

import java.util.*;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author Vlad Mihalcea
 */
public class IdWasNullEqualityTest
        extends AbstractEqualityCheckTest<IdWasNullEqualityTest.Post> {

    @Override
    protected Class<?>[] entities() {
        return new Class[]{
            Post.class
        };
    }

    @Test
    public void testEquality() {
        Post post = new Post();
        post.setTitle("High-PerformanceJava Persistence");

        assertEqualityConsistency(Post.class, post);
    }

    @Test
    public void testCollectionSize() {
        if(!ENABLE_LONG_RUNNING_TESTS) {
            return;
        }
        int collectionSize = 25_000;

        long createSetStartNanos = System.nanoTime();
        Set<Post> postSet = new HashSet<>();

        for (int i = 0; i < collectionSize; i++) {
            Post post = new Post();
            postSet.add(post);
        }

        long createSetEndNanos = System.nanoTime();
        LOGGER.info(
            "Creating a Set with [{}] elements took : [{}] s",
            collectionSize,
            TimeUnit.NANOSECONDS.toSeconds(createSetEndNanos - createSetStartNanos)
        );
    }

    @Entity(name = "Post")
    @Table(name = "post")
    public static class Post implements Identifiable<Long> {

        @Id
        @GeneratedValue
        private Long id;

        private boolean idWasNull;

        private String title;

        public Post() {
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;

            if (!(o instanceof Post))
                return false;

            Post other = (Post) o;

            return id != null && id.equals(other.getId());
        }

        @Override
        public int hashCode() {
            Long id = getId();
            if (id == null) idWasNull = true;
            return idWasNull ? 0 : id.hashCode();
        }

        public Long getId() {
            return id;
        }

        public Post setId(Long id) {
            this.id = id;
            return this;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }
}
