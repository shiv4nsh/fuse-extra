/**
 * Copyright (C) 2012 FuseSource Corp. All rights reserved.
 * http://fusesource.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.fusesource.mq.leveldb

import org.apache.activemq.store.PersistenceAdapter
import java.io.File

/**
 * <p>
 * </p>
 *
 * @author <a href="http://hiramchirino.com">Hiram Chirino</a>
 */
class HALevelDBStoreTest extends LevelDBStoreTest {
  override protected def setUp: Unit = {
    TestingHDFSServer.start
    super.setUp
  }

  override protected def tearDown: Unit = {
    super.tearDown
    TestingHDFSServer.stop
  }

  override protected def createPersistenceAdapter(delete: Boolean): PersistenceAdapter = {
    var store: HALevelDBStore = new HALevelDBStore
    store.setDirectory(new File("target/activemq-data/haleveldb"))
    store.setDfsDirectory("localhost")
    if (delete) {
      store.deleteAllMessages
    }
    return store
  }
}