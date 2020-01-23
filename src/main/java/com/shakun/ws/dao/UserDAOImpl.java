package com.shakun.ws.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.BeanUtils;

import com.shakun.ws.entity.UserEntity;
import com.shakun.ws.shared.dto.UserDTO;
import com.shakun.ws.utils.HibernateUtils;

public class UserDAOImpl implements UserDAO {

	Session session;

	public void openConnection() {
		SessionFactory sf = HibernateUtils.getSessionFactory();
		session = sf.openSession();

	}

	public void closeConnection() {
		if (null != session) {
			session.close();
		}

	}

	public UserDTO getUserByUserName(String userName) {
		UserDTO userDto = null;
		CriteriaBuilder cb = session.getCriteriaBuilder();
		CriteriaQuery<UserEntity> criteria = cb.createQuery(UserEntity.class);
		Root<UserEntity> profileRoot = criteria.from(UserEntity.class);
		criteria.select(profileRoot);
		criteria.where(cb.equal(profileRoot.get("email"), userName));

		Query<UserEntity> query = session.createQuery(criteria);
		List<UserEntity> resultList = query.getResultList();
		if (null != resultList && resultList.size() > 0) {
			UserEntity user = resultList.get(0);
			userDto = new UserDTO();
			BeanUtils.copyProperties(user, userDto);
		}
		return userDto;
	}

	public UserDTO saveUser(UserDTO user) {
		UserDTO returnValue = new UserDTO();
		UserEntity userEntity = new UserEntity();
		BeanUtils.copyProperties(user, userEntity);
		session.beginTransaction();
		session.save(userEntity);
		session.getTransaction().commit();
		BeanUtils.copyProperties(userEntity, returnValue);
		return returnValue;
	}

	@Override
	public UserDTO getUser(String userId) {
		UserDTO userDto = null;
		CriteriaBuilder cb = session.getCriteriaBuilder();
		CriteriaQuery<UserEntity> criteria = cb.createQuery(UserEntity.class);
		Root<UserEntity> profileRoot = criteria.from(UserEntity.class);
		criteria.select(profileRoot);
		criteria.where(cb.equal(profileRoot.get("userId"), userId));

		Query<UserEntity> query = session.createQuery(criteria);
		UserEntity result = query.getSingleResult();

		userDto = new UserDTO();
		BeanUtils.copyProperties(result, userDto);
		return userDto;
	}

	@Override
	public void updateUser(UserDTO user) {
		UserEntity userEntity = new UserEntity();
		BeanUtils.copyProperties(user, userEntity);
		session.beginTransaction();
		session.update(userEntity);
		session.getTransaction().commit();
	}

	@Override
	public List<UserDTO> getUsers(int start, int limit) {
		List<UserDTO> returnValue = new ArrayList<UserDTO>();
		CriteriaBuilder cb = session.getCriteriaBuilder();
		CriteriaQuery<UserEntity> criteria = cb.createQuery(UserEntity.class);
		Root<UserEntity> userRoot = criteria.from(UserEntity.class);
		criteria.select(userRoot);
		List<UserEntity> searchResults = session.createQuery(criteria).setFirstResult(start).setMaxResults(limit)
				.getResultList();
		if(null!=searchResults) {
			for(UserEntity user: searchResults) {
				UserDTO userDto = new UserDTO();
				BeanUtils.copyProperties(user, userDto);
				returnValue.add(userDto);
			}
		}
		return returnValue;
	}

	@Override
	public void deleteUser(UserDTO storedUser) {
		UserEntity userEntity = new UserEntity();
		BeanUtils.copyProperties(storedUser, userEntity);
		session.beginTransaction();
		session.delete(userEntity);
		session.getTransaction().commit();
	}

	@Override
	public UserDTO getUserByEmailToken(String emailVerificationToken) {
		UserDTO userDto = null;
		CriteriaBuilder cb = session.getCriteriaBuilder();
		CriteriaQuery<UserEntity> criteria = cb.createQuery(UserEntity.class);
		Root<UserEntity> profileRoot = criteria.from(UserEntity.class);
		criteria.select(profileRoot);
		criteria.where(cb.equal(profileRoot.get("emailVerificationToken"), emailVerificationToken));

		Query<UserEntity> query = session.createQuery(criteria);
		List<UserEntity> resultList = query.getResultList();
		if (null != resultList && resultList.size() > 0) {
			UserEntity user = resultList.get(0);
			userDto = new UserDTO();
			BeanUtils.copyProperties(user, userDto);
		}
		return userDto;
	}
}
