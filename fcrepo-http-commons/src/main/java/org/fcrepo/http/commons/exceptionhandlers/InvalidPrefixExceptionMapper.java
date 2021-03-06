/*
 * Licensed to DuraSpace under one or more contributor license agreements.
 * See the NOTICE file distributed with this work for additional information
 * regarding copyright ownership.
 *
 * DuraSpace licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except in
 * compliance with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.fcrepo.http.commons.exceptionhandlers;

import static javax.ws.rs.core.Response.status;
import static javax.ws.rs.core.Response.Status.BAD_REQUEST;
import static org.fcrepo.http.commons.domain.RDFMediaType.TEXT_PLAIN_WITH_CHARSET;
import static org.slf4j.LoggerFactory.getLogger;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.fcrepo.kernel.api.exception.InvalidPrefixException;

import org.slf4j.Logger;

/**
 * For invalid namespace exceptions on CRUD actions for nodes/datastreams
 *
 * @author nianma
 * @since November 2, 2015
 */
@Provider
public class InvalidPrefixExceptionMapper implements
        ExceptionMapper<InvalidPrefixException>, ExceptionDebugLogging {

    private static final Logger LOGGER = getLogger(InvalidPrefixExceptionMapper.class);

    @Override
    public Response toResponse(final InvalidPrefixException e) {
        LOGGER.error("FedoraInvalidPrefixExceptionMapper caught an exception: {}", e.getMessage());
        debugException(this, e, LOGGER);
        return status(BAD_REQUEST).entity(e.getMessage()).type(TEXT_PLAIN_WITH_CHARSET).build();
    }

}
