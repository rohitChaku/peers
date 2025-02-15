/*
    This file is part of Peers, a java SIP softphone.

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
    
    Copyright 2010 Yohann Martineau 
*/

package net.sourceforge.peers.javawebstart;

import net.sourceforge.peers.Logger;

public class RegistrationStateRegistering extends RegistrationState {

    public RegistrationStateRegistering(String id, Registration registration,
            Logger logger) {
        super(id, registration, logger);
    }

    @Override
    public void registerSuccessful() {
        registration.setState(registration.SUCCESS);
        logger.info("Registered");
        System.out.println("Registered");
    }

    @Override
    public void registerFailed() {
        registration.setState(registration.FAILED);
        logger.error("Registration failed");
        System.out.println("Registration Failed");
    }

}
