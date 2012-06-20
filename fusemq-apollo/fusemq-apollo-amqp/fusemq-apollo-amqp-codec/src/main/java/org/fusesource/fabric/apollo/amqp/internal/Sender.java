/**
 * Copyright (C) 2010-2011, FuseSource Corp.  All rights reserved.
 *
 *     http://fusesource.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.fusesource.fabric.apollo.amqp.internal;

import org.fusesource.fabric.apollo.amqp.AMQPSender;
import org.fusesource.fabric.apollo.amqp.AMQPSenderOptions;
import org.fusesource.fabric.apollo.amqp.Callback;
import org.fusesource.fabric.apollo.amqp.codec.api.AnnotatedMessage;
import org.fusesource.fabric.apollo.amqp.codec.interfaces.DeliveryState;
import org.fusesource.fabric.apollo.amqp.codec.types.Flow;
import org.fusesource.fabric.apollo.amqp.codec.types.Transfer;

/**
 *
 * @author <a href="http://hiramchirino.com">Hiram Chirino</a>
 */
public class Sender extends Endpoint implements AMQPSender {

    public Sender(AMQPSenderOptions options) {
        super(options);
    }

    @Override
    protected void processFlowFrame(Session source, Flow flow) {
        if( flow.getLinkCredit()!=null ) {
            linkCredit = flow.getLinkCredit();
            current().pumpOverflow();
        }
    }

    public void send(AnnotatedMessage message, Callback<DeliveryState> callback) {
        current().send(message, callback);
    }

    public boolean full() {
        if( linkCredit <= 0 )
            return true;

        Session session = (Session) getSession();
        if( session == null )
            return true;
        if( session.remoteIncomingWindow <= 0 )
            return true;

        return false;
    }

}