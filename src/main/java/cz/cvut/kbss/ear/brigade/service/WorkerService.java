package cz.cvut.kbss.ear.brigade.service;

import cz.cvut.kbss.ear.brigade.dao.implementations.WorkerDao;
import cz.cvut.kbss.ear.brigade.model.Brigade;
import cz.cvut.kbss.ear.brigade.model.Worker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class WorkerService {

    private final WorkerDao workerDao;

    private final PlatformTransactionManager txManager;

    @Autowired
    public WorkerService(WorkerDao workerDao, PlatformTransactionManager txManager) {
        this.workerDao = workerDao;
        this.txManager = txManager;
    }


    // vrat aktualni seznam brigad, na ktere je worker prihlasen
    public List<Brigade> getWorkerBrigades(Worker worker) {
        return worker.getBrigades()
                .stream()
                .filter(br -> br.getDateTo().getTime() > System.currentTimeMillis())
                .collect(Collectors.toList());
    }


    private void doWorker() {
        final Worker user = new Worker();
        user.setFirstName("FirstName" + 1);
        user.setLastName("LastName");
        user.setEmail("username" + "@kbss.felk.cvut.cz");
        user.setPassword(Integer.toString(2));
        workerDao.persist(user);
    }

    @PostConstruct
    private void initSystem() {
        TransactionTemplate txTemplate = new TransactionTemplate(txManager);
        txTemplate.execute((status) -> {
            doWorker();
            return null;
        });
    }


}
