package net.kmanikin.example.bank;

import net.kmanikin.core.Id;
import net.kmanikin.core.World;
import net.kmanikin.message.LocalMessage;

public interface AccountModule {
    class ID implements Id<Account> {
        public Account init() { return new Account(0.0); }
    }

    class Account {
        public final double balance;
        public Account(double balance) { this.balance = balance; }
    }

    abstract class Msg<W extends World<W>> extends LocalMessage.LMsg<W, ID, Account, Void> { }

    class Open<W extends World<W>> extends Msg<W> {
        public boolean pre() {
            return false;
        }
        public Account app() {
            return null;
        }
        public Void eff() {
            return null;
        }
        public boolean pst() {
            return false;
        }
    }
}
