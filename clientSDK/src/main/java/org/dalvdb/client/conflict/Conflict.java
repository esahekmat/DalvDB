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

package org.dalvdb.client.conflict;

import dalv.common.Common;

import java.util.List;

public class Conflict {
  private final String key;
  private final List<Common.Operation> serverOps;
  private final List<Common.Operation> clientOps;

  public Conflict(List<Common.Operation> serverOps, List<Common.Operation> clientOps) {
    if (serverOps.size() < 1 || clientOps.size() < 1)
      throw new IllegalArgumentException("no conflicting operations");
    this.key = serverOps.get(0).getKey();
    this.clientOps = clientOps;
    this.serverOps = serverOps;
  }

  public String getKey() {
    return key;
  }

  public List<Common.Operation> getServerOps() {
    return serverOps;
  }

  public List<Common.Operation> getClientOps() {
    return clientOps;
  }
}
