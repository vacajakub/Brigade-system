package cz.cvut.kbss.ear.brigade.dao.implementations;

import cz.cvut.kbss.ear.brigade.dao.interfaces.IWorkerDao;
import cz.cvut.kbss.ear.brigade.model.Worker;
import org.springframework.stereotype.Repository;

@Repository
public class WorkerDao extends BaseDao<Worker> implements IWorkerDao {


    protected WorkerDao() {
        super(Worker.class);
    }
}
