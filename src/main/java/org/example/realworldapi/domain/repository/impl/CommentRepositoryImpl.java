package org.example.realworldapi.domain.repository.impl;

import org.example.realworldapi.domain.entity.persistent.Article;
import org.example.realworldapi.domain.entity.persistent.Comment;
import org.example.realworldapi.domain.entity.persistent.User;
import org.example.realworldapi.domain.repository.CommentRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Root;

@ApplicationScoped
public class CommentRepositoryImpl extends AbstractRepository<Comment, Long>
    implements CommentRepository {

  private EntityManager entityManager;

  public CommentRepositoryImpl(EntityManager entityManager) {
    this.entityManager = entityManager;
  }

  @Override
  public Comment create(Comment comment) {
    return persist(comment);
  }

  @Override
  public Comment findComment(String slug, Long commentId, Long authorId) {

    CriteriaBuilder builder = getCriteriaBuilder();
    CriteriaQuery<Comment> criteriaQuery = getCriteriaQuery(builder);
    Root<Comment> comment = getRoot(criteriaQuery);

    Join<Comment, Article> article = comment.join("article");
    Join<Comment, User> author = comment.join("author");

    criteriaQuery.select(comment);
    criteriaQuery.where(
        builder.and(
            builder.equal(builder.upper(article.get("slug")), slug.toUpperCase().trim()),
            builder.equal(comment.get("id"), commentId),
            builder.equal(author.get("id"), authorId)));

    return getSingleResult(criteriaQuery);
  }

  @Override
  public void delete(Comment comment) {
    entityManager.remove(comment);
  }

  @Override
  EntityManager getEntityManager() {
    return this.entityManager;
  }

  @Override
  Class<Comment> getEntityClass() {
    return Comment.class;
  }
}
