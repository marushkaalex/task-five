package com.epam.am.whatacat.service;

import com.epam.am.whatacat.dao.DaoException;
import com.epam.am.whatacat.dao.UserDao;
import com.epam.am.whatacat.model.User;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;
import java.util.List;
import java.util.Random;

public class UserService extends BaseService {
    public UserService() throws ServiceException {
    }

    /**
     * Registers new user
     * @param user user to be registered
     * @param password user password
     * @return User with set id
     * @throws ServiceException
     */
    public User register(User user, String password) throws ServiceException {
        try {
            byte[] salt = new byte[16];
            new Random().nextBytes(salt);
            user.setHashedPassword(hashPassword(password, salt));
            UserDao userDao = daoFactory.getUserDao();
            return userDao.save(user);
        } catch (DaoException e) {
            throw new ServiceException("Unable to register user", e);
        }
    }

    /**
     * Logs in user if credentials are correct
     * @param email user email
     * @param password user password
     * @return User or null if credentials are wrong
     * @throws ServiceException
     */
    public User logIn(String email, String password) throws ServiceException {
        try {
            UserDao userDao = daoFactory.getUserDao();
            User user = userDao.findByEmail(email);
            String[] split = user.getHashedPassword().split(" ");
            String saltStr = split[1];
            byte[] salt = Base64.getDecoder().decode(saltStr);
            String hashedPassword = hashPassword(password, salt);
            if (hashedPassword.equals(user.getHashedPassword())) {
                return user;
            } else {
                return null;
            }
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    /**
     * Checks is specified email already in use
     * @param email email
     * @return true if email free, false otherwise
     * @throws ServiceException
     */
    public boolean isEmailFree(String email) throws ServiceException {
        try {
            UserDao userDao = daoFactory.getUserDao();
            return userDao.isEmailFree(email);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    /**
     * Checks is specified email already in use
     * @param nickname nickname
     * @return true if nickname free, false otherwise
     * @throws ServiceException
     */
    public boolean isNicknameFree(String nickname) throws ServiceException {
        try {
            UserDao userDao = daoFactory.getUserDao();
            return userDao.isNicknameFree(nickname);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    /**
     * Saves user
     * @param user user
     * @throws ServiceException
     */
    public void save(User user) throws ServiceException {
        try {
            UserDao userDao = daoFactory.getUserDao();
            userDao.save(user);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    /**
     * Finds user by id
     * @param id user id
     * @return User or null if not found
     * @throws ServiceException
     */
    public User findById(long id) throws ServiceException {
        try {
            UserDao userDao = daoFactory.getUserDao();
            return userDao.findById(id);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    /**
     *
     * @param limit limit
     * @param offset offset
     * @return List of all users
     * @throws ServiceException
     */
    public List<User> getAll(long limit, long offset) throws ServiceException {
        try {
            UserDao userDao = daoFactory.getUserDao();
            return userDao.getAll(limit, offset);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    /**
     * Counts all users
     * @return the number of users
     * @throws ServiceException
     */
    public long count() throws ServiceException {
        try {
            UserDao userDao = daoFactory.getUserDao();
            return userDao.count();
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    /**
     * Hashes password with specified salt
     * @param password password
     * @param salt byte salt
     * @return password hash and salt hash split by whitespace
     * @throws ServiceException
     */
    public String hashPassword(String password, byte[] salt) throws ServiceException {
        try {
            KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 65536, 128);
            SecretKeyFactory f = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            byte[] hash = f.generateSecret(spec).getEncoded();
            Base64.Encoder enc = Base64.getEncoder();
            return enc.encodeToString(hash) + ' ' + enc.encodeToString(salt);
        } catch (InvalidKeySpecException | NoSuchAlgorithmException e) {
            throw new ServiceException(e);
        }
    }

    /**
     * Deletes user
     * @param id user id
     * @throws ServiceException
     */
    public void delete(long id) throws ServiceException {
        try {
            UserDao userDao = daoFactory.getUserDao();
            userDao.delete(id);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }
}
