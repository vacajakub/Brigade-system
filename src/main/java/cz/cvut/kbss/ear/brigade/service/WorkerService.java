package cz.cvut.kbss.ear.brigade.service;

import cz.cvut.kbss.ear.brigade.dao.implementations.BrigadeDao;
import cz.cvut.kbss.ear.brigade.dao.implementations.WorkerDao;
import cz.cvut.kbss.ear.brigade.exception.AlreadyRatedException;
import cz.cvut.kbss.ear.brigade.exception.BrigadeIsNotFinishedException;
import cz.cvut.kbss.ear.brigade.exception.LateSignOffException;
import cz.cvut.kbss.ear.brigade.exception.WorkerDidNotWorkOnBrigadeException;
import cz.cvut.kbss.ear.brigade.model.Brigade;
import cz.cvut.kbss.ear.brigade.model.Worker;
import cz.cvut.kbss.ear.brigade.util.Constants;
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
    private final BrigadeDao brigadeDao;

    @Autowired
    public WorkerService(WorkerDao workerDao, BrigadeDao brigadeDao) {
        this.workerDao = workerDao;
        this.brigadeDao = brigadeDao;
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

    @Transactional
    public void singOffFromBrigade(Worker worker, Brigade brigade) {
        if (System.currentTimeMillis() < brigade.getDateFrom().getTime() - Constants.LIMIT_FOR_SIGNING_OFF_OF_BRIGADE) {
            worker.getBrigades().remove(brigade);
            brigade.getWorkers().remove(worker);
            workerDao.update(worker);
            brigadeDao.update(brigade);
        } else {
            throw new LateSignOffException("Too late to sign off!!!");
        }
    }

    @Transactional
    public void addThumbsUpToBrigade(Worker worker, Brigade brigade){
        conditionForRatingBrigade(worker, brigade);

        worker.getBrigadesThumbsUps().add(brigade);
        brigade.getWorkersThumbsUps().add(worker);
        workerDao.update(worker);
        brigadeDao.update(brigade);
    }

    @Transactional
    public void addThumbsDownToBrigade(Worker worker, Brigade brigade){
        conditionForRatingBrigade(worker, brigade);

        worker.getBrigadesThumbsDowns().add(brigade);
        brigade.getWorkersThumbsDowns().add(worker);
        brigadeDao.update(brigade);
        workerDao.update(worker);
    }

    private void conditionForRatingBrigade(Worker worker, Brigade brigade){
        if(worker.getBrigades().stream().noneMatch(b -> b.getId().intValue() == brigade.getId().intValue())){
            throw new WorkerDidNotWorkOnBrigadeException("Worker did not work on the brigade!!!");
        }
        if (brigade.getDateTo().getTime() > System.currentTimeMillis()){
            throw new BrigadeIsNotFinishedException("Brigade is not finished!!!");
        }
        if (worker.getBrigadesThumbsUps().stream().anyMatch(b -> b.getId().intValue() == brigade.getId().intValue()) ||
                worker.getBrigadesThumbsDowns().stream().anyMatch(b -> b.getId().intValue() == brigade.getId().intValue())){
            throw new AlreadyRatedException("Worker already rated this brigade!!!");
        }
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


    @PostConstruct
    public void initDb() {
        // todo - pouze pro vygenerovani tabulek (LAZY init)
        final List<Worker> workers = workerDao.findAll();
    }
}
