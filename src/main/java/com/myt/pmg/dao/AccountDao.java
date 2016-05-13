package com.myt.pmg.dao;

import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import com.myt.pmg.model.Account;

@Repository
public class AccountDao extends BasicDaoImpl<Account> {

	public Account updateUser(Account account) {
		getMongoTemplate().save(account);
		return account;

	}

	public String addAccount(Account account) {
		return super.add(account);
	}

	public Account findByUserId(String userId) {
		Query query = new Query();
		query.addCriteria(Criteria.where("userId").is(userId));
		// return getMongoTemplate().find(query, Account.class).get(0);

		return getMongoTemplate().findOne(query, Account.class);

	}

}
