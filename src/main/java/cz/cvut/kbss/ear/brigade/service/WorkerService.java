package cz.cvut.kbss.ear.brigade.service;

import cz.cvut.kbss.ear.brigade.dao.implementations.WorkerDao;
import cz.cvut.kbss.ear.brigade.model.Brigade;
import cz.cvut.kbss.ear.brigade.model.Worker;
import javafx.concurrent.WorkerStateEvent;
import javafx.util.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.PostConstruct;
import java.sql.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class WorkerService {

    private final WorkerDao workerDao;

    @Autowired
    public WorkerService(WorkerDao workerDao) {
        this.workerDao = workerDao;
    }

    @Transactional(readOnly = true)
    public Worker find(Integer id) {
        return workerDao.find(id);
    }

    @Transactional(readOnly = true)
    public List<Worker> findAll() {
        return workerDao.findAll();
    }

    @Transactional(readOnly = true)
    public void persist(Worker worker) {
        workerDao.persist(worker);
    }

    @Transactional(readOnly = true)
    public void update(Worker worker) {
        workerDao.update(worker);
    }

    @Transactional(readOnly = true)
    public Worker findByEmail(String email) {
        return workerDao.findByEmail(email);
    }


    @Transactional
    // returns actual list of brigades the worker is signed for and are in future or present
    public List<Brigade> getFutureBrigades(Worker worker) {
        return filterBrigades(worker.getBrigades(), false);
    }

    @Transactional
    //returns workers past brigades
    public List<Brigade> getPastBrigades(Worker worker) {
        return filterBrigades(worker.getBrigades(), true);
    }


    @PostConstruct
    public void initDb() {
        // todo - pouze pro vygenerovani tabulek (LAZY init)
        final List<Worker> workers = workerDao.findAll();
    }

    @Transactional(readOnly = true)
    public Pair<Integer, Integer> getWorkerScore(Worker worker) {
        int countShow = filterBrigades(worker.getBrigades(), true).size();
        int countNoShow = filterBrigades(worker.getUnvisitedBrigades(), true).size();

        return new Pair<>(countShow, countNoShow);
    }


    static List<Brigade> filterBrigades(List<Brigade> brigades, boolean before) {
        return brigades
                .stream()
                .filter(brigade -> {
                    if (before) {
                        return brigade.getDateTo().getTime() < System.currentTimeMillis();
                    }
                    return brigade.getDateTo().getTime() >= System.currentTimeMillis();
                })
                .collect(Collectors.toList());
    }
}
