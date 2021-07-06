package tn.sofrecom.mdrissi.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import tn.sofrecom.mdrissi.entities.Attachment;

@Repository
public interface AttachmentRepository extends JpaRepository<Attachment,Integer> {

}
