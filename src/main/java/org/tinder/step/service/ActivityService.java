package org.tinder.step.service;

import org.tinder.step.dao.ActivityDAO;
import org.tinder.step.dao.DAO;
import org.tinder.step.entity.Activity;

import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

public class ActivityService {
    DAO<Activity> daoActivity;
    LikesService service;

    public ActivityService() {
        daoActivity = new ActivityDAO();
        service = new LikesService();
    }

    public boolean addLogout_time(Activity act) {
        return daoActivity.add(act);
    }

    public Activity getActivityById(int id) throws Exception {
        return daoActivity.get(id).orElseThrow(Exception::new);
    }

    public List<Activity> getAllLikedUsersLogoutTime(int user_id_from) {
        List<Integer> allLikedUserIds = service.getAllLikedUserIds(user_id_from);
        return daoActivity.getAll().stream().filter(i -> allLikedUserIds.contains(i.getUser_id())).collect(Collectors.toList());
    }
}
