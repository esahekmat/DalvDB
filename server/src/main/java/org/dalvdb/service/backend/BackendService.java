/*
 * Copyright (C) 2020-present Isa Hekmatizadeh
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Affero General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Affero General Public License for more details.
 *
 *  You should have received a copy of the GNU Affero General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.dalvdb.service.backend;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import org.dalvdb.DalvConfig;
import org.dalvdb.storage.StorageService;
import org.dalvdb.watch.WatchManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class BackendService implements Closeable {
  private static final Logger logger = LoggerFactory.getLogger(BackendService.class);
  private final Server server;
  private final WatchManager watchManager;

  public BackendService(StorageService storageService, WatchManager watchManager) {
    this.watchManager = watchManager;
    final int port = DalvConfig.getInt(DalvConfig.BACKEND_PORT);
    server = ServerBuilder.forPort(port)
        .addService(new BackendServiceImpl(storageService, watchManager)).build();
    try {
      server.start();
    } catch (IOException e) {
      logger.error("Starting server failed", e);
      System.exit(1);
    }
    logger.info("Server started, listening on " + port);

    new Thread(() -> {
      try {
        server.awaitTermination();
      } catch (InterruptedException ignored) {
      }
    }).start();
  }


  @Override
  public void close() {
    watchManager.close();
    if (server != null) {
      try {
        server.shutdown().awaitTermination(30, TimeUnit.SECONDS);
        logger.info("Server closed gracefully");
      } catch (InterruptedException e) {
        logger.error(e.getMessage(), e);
      }
    }
  }
}
