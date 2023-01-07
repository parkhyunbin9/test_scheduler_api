package com.daou.batch.writer;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import org.springframework.batch.item.database.JpaItemWriter;

public class JpaItemPresistWriter<T> extends JpaItemWriter<T> {

	public JpaItemPresistWriter() {
	}

	public JpaItemPresistWriter(EntityManagerFactory emf) {
		setEntityManagerFactory(emf);
	}

	@Override
	protected void doWrite(EntityManager entityManager, List<? extends T> items) {
		if (logger.isDebugEnabled()) {
			logger.debug("Writing to JPA with " + items.size() + " items.");
		}

		if (!items.isEmpty()) {
			long persistCount = 0;
			for (T item : items) {
				if (!entityManager.contains(item)) {
					entityManager.persist(item);
					persistCount++;
				}
			}
			if (logger.isDebugEnabled()) {
				logger.debug(persistCount + " entities persisted.");
				logger.debug((items.size() - persistCount) + " entities found in persistence context.");
			}
		}
	}
}
