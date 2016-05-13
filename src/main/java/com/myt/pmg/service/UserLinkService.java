package com.myt.pmg.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.myt.pmg.dao.UserLinkDao;
import com.myt.pmg.model.UserLink;

@Service
public class UserLinkService {

	@Autowired
	private UserLinkDao userLinkDao;

	public void addUserLink(UserLink userLink) {
		userLinkDao.add(userLink);
	}

	public List<UserLink> getUserLinkList(String id) {
		return userLinkDao.findAllById(id);
	}

	public long countLinksPostedToUser(String userid) {
		if (userLinkDao.countLinksPostedToUser(userid) == 0)
			return 1;
		else
			return userLinkDao.countLinksPostedToUser(userid);
	}

	public long countLinksPostedToUserAndClicked(String userid) {
		if (userLinkDao.countLinksPostedToUserAndClicked(userid) == 0)
			return 1;
		else
			return userLinkDao.countLinksPostedToUserAndClicked(userid);
	}

	public long countLinksPostedToUserAndVerified(String userid) {
		if (userLinkDao.countLinksPostedToUserAndVerified(userid) == 0)
			return 1;
		else
			return userLinkDao.countLinksPostedToUserAndVerified(userid);
	}

	public long countLinksServedByUser(String userid) {
		if (userLinkDao.countLinksServedByUser(userid) == 0)
			return 1;
		else
			return userLinkDao.countLinksServedByUser(userid);
	}

	public long countLinksServedByUserAndApproved(String userid) {
		if (userLinkDao.countLinksServedByUserAndApproved(userid) == 0)
			return 1;
		else
			return userLinkDao.countLinksServedByUserAndApproved(userid);
	}

	public long countLinksServedByUserAndDisapproved(String userid) {
		if (userLinkDao.countLinksServedByUserAndDisapproved(userid) == 0)
			return 1;
		else
			return userLinkDao.countLinksServedByUserAndDisapproved(userid);
	}

	public boolean linkPosted(String userid, String linkid) {

		UserLink userlink = new UserLink();
		userlink.setUserId(userid);
		userlink.setLinkId(linkid);
		userlink.setClicked(false);
		userlink.setVerified(false);
		// addUserLink(userlink);
		// return userLinkDao.linkPosted(userid, linkid);
		return true;
	}

	public boolean linkPosted(String userid, String linkid, String broadcasteruserid) {

		UserLink userlink = new UserLink();
		userlink.setUserId(userid);
		userlink.setLinkId(linkid);
		userlink.setBroadcasterUserId(broadcasteruserid);
		userlink.setClicked(false);
		userlink.setVerified(false);
		userlink.setLinkrecvDate(new Date());
		userlink.setLinkbroadcastingDate(new Date());
		System.out.println("Broadcasting the link details are>>>>>>>>");

		System.out.println("Total Rows in MongoDB for the collection ::UserLink" + userLinkDao.count(UserLink.class));
		boolean isEligible = false;
		if (userLinkDao.count(UserLink.class) > 0) {
			isEligible = iseligibleforbroadcasting(broadcasteruserid);
		}

		System.out.println("Is User Eligible ?" + isEligible);
		// Logic to add the link i,e broadcast after 24 hours
		if (isEligible) {
			addUserLink(userlink);
			return true;
		}
		return false;
	}

	public UserLink findByLinkId(String linkid) {
		return userLinkDao.findByLinkId(linkid);
	}

	public List<UserLink> findByUserid(String userid) {
		return userLinkDao.findByUserId(userid);
	}

	public List<UserLink> findByBroadcasterId(String broadcasterId) {
		return userLinkDao.findByBroadcasterId(broadcasterId);
	}

	public void update(UserLink userlink) {
		userLinkDao.update(userlink);
	}

	public List<UserLink> findAll() {
		return userLinkDao.findAll();
	}

	public long countClicksOnLink(String linkId) {
		return userLinkDao.countClicksOnLink(linkId);
	}

	private boolean iseligibleforbroadcasting(String broadcastinguserid) {
		long MILLIS_PER_DAY = 60 * 60 * 1000L;
		Date now = new Date();

		UserLink userlink = userLinkDao.getlatestdate(broadcastinguserid);
		Date latestlinkBroadcastedDate = userlink.getLinkbroadcastingDate();

		System.out.println("Present Date and Time" + now);
		System.out.println("Last Link Broadcasted date" + latestlinkBroadcastedDate);

		return (now.getTime() - latestlinkBroadcastedDate.getTime()) > MILLIS_PER_DAY;

	}
}
