package com.tup.reservasi.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.tup.reservasi.entity.Notification;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, String> {

    List<Notification> findByPenerimaId(String penerimaId);

    List<Notification> findByPenerimaIdOrderByCreatedAtDesc(String penerimaId);

    List<Notification> findByPenerimaIdAndStatusBaca(String penerimaId, boolean statusBaca);

    List<Notification> findByPenerimaIdAndStatusBacaOrderByCreatedAtDesc(String penerimaId, boolean statusBaca);

    List<Notification> findByStatusBaca(boolean statusBaca);
}
