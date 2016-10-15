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

    public boolean isEmailFree(String email) throws ServiceException {
        try {
            UserDao userDao = daoFactory.getUserDao();
            return userDao.isEmailFree(email);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    public boolean isNicknameFree(String nickname) throws ServiceException {
        try {
            UserDao userDao = daoFactory.getUserDao();
            return userDao.isNicknameFree(nickname);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    public void save(User user) throws ServiceException {
        try {
            UserDao userDao = daoFactory.getUserDao();
            userDao.save(user);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    public User findById(long id) throws ServiceException {
        try {
            UserDao userDao = daoFactory.getUserDao();
            return userDao.findById(id);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    public List<User> getAll(long limit, long offset) throws ServiceException {
        try {
            UserDao userDao = daoFactory.getUserDao();
            return userDao.getAll(limit, offset);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    public long count() throws ServiceException {
        try {
            UserDao userDao = daoFactory.getUserDao();
            return userDao.count();
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

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

    public void delete(long id) throws ServiceException {
        try {
            UserDao userDao = daoFactory.getUserDao();
            userDao.delete(id);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }
}
