package xyz.yunzhongyan.www.dao.mongo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import xyz.yunzhongyan.www.domain.po.RewardTask;
import xyz.yunzhongyan.www.domain.po.TaskProposal;

import java.util.List;


public interface RewardTaskDao extends MongoRepository<RewardTask, String> {
    Page<RewardTask> findByHootIsOrderByDeadlineDesc(Boolean hoot, Pageable pageable);

    Page<RewardTask> findByStateIn(List<Integer> states, Pageable pageable);
    List<RewardTask> findByHootIs(Boolean hoot);

    Page<RewardTask> findByUserIdIsOrderByDeadlineDesc(String userId, Pageable pageable);

    Page<RewardTask> findByStateInOrderByDeadlineDesc(List<Integer> states, Pageable pageable);

    Page<RewardTask> findByStateInOrderByDeadlineAsc(List<Integer> states, Pageable pageable);
}
