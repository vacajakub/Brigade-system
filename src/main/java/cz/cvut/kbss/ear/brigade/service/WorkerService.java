package cz.cvut.kbss.ear.brigade.service;

import cz.cvut.kbss.ear.brigade.dao.implementations.WorkerDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WorkerService {

    private WorkerDao workerDao;

    @Autowired
    public WorkerService(WorkerDao workerDao) {
        this.workerDao = workerDao;
    }
}
