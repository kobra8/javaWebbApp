package pl.javasurvival.HelloServer;

import org.springframework.data.repository.CrudRepository;

public interface MongoRepository extends CrudRepository<BoardMessage, Long> {
}
