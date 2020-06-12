package aview

import com.google.inject.AbstractModule
import net.codingwell.scalaguice.ScalaModule
import player.daoComponent.DAOInterface
import player.daoComponent.slickImpl.SlickDao
import player.daoComponent.mongoDBImpl.MongoDBDao

class PlayersModule extends AbstractModule with ScalaModule {

  override def configure(): Unit = {
    bind[DAOInterface].to[MongoDBDao]
  }
}
