package com.example.courseregistration.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.stereotype.Repository;

import com.example.courseregistration.config.HibernateUtil;
import com.example.courseregistration.model.User;

@Repository
public class UserDAOImpl implements UserDAO {

	private final SessionFactory sessionFactory;

    public UserDAOImpl() {
        this.sessionFactory = HibernateUtil.getSessionFactory();
    }
    
	@Override
	public void saveUser(User user) {
		Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.merge(user);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
	}
	
	@Override
	public void updateUser(User user) {
		Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.merge(user);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
	}


	@Override
	public User getUserByUsername(String username) {
		try (Session session = sessionFactory.openSession()) {
            return session.createQuery("FROM User WHERE username = :username", User.class)
                    .setParameter("username", username)
                    .uniqueResult();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
	}
	
	@Override
	public User getUserById(Long id) {
		try (Session session = sessionFactory.openSession()) {
            return session.get(User.class, id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
	}

	@Override
	public List<User> getAllUsers() {
		try (Session session = sessionFactory.openSession()) {
            return session.createQuery("FROM User", User.class).list();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
	}
}

