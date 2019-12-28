package xyz.yunzhongyan.www.dao.mongo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import xyz.yunzhongyan.www.domain.po.TaskProposal;

import java.util.List;

public interface TaskProposalDao extends MongoRepository<TaskProposal, String> {
    List<TaskProposal> findByStateInOrderByTimeDesc(List<Integer> list);
    List<TaskProposal> findByStateInAndRewardTaskIdIsOrderByTimeDesc(List<Integer> list,String id);

    Page<TaskProposal> findByUserIdIsOrderByTimeDesc(String userId, Pageable pageable);

    Page<TaskProposal> findByRewardTaskIdIsOrderByTimeDesc(String id, Pageable pageable);
    Page<TaskProposal> findByRewardTaskIdIsOrderByTimeAsc(String id, Pageable pageable);

    List<TaskProposal> findByUserIdIsAndRewardTaskIdIs(String userId, String id);
}
