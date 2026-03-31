CREATE TABLE IF NOT EXISTS "Role" (
	"role_id" SERIAL,
	"role_name" VARCHAR(50) NOT NULL UNIQUE,
	PRIMARY KEY("role_id")
);




CREATE TABLE IF NOT EXISTS "User" (
	"user_id" UUID DEFAULT gen_random_uuid(),
	"username" VARCHAR(100) NOT NULL UNIQUE,
	"password_hash" VARCHAR(255) NOT NULL,
	"email" VARCHAR(150) NOT NULL UNIQUE,
	"role_id" INTEGER NOT NULL,
	"created_at" TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	PRIMARY KEY("user_id")
);


CREATE INDEX "idx_user_role"
ON "User" ("role_id");

CREATE TABLE IF NOT EXISTS "AccountHolder" (
	"holder_id" UUID DEFAULT gen_random_uuid(),
	"user_id" UUID NOT NULL UNIQUE,
	"first_name" VARCHAR(100) NOT NULL,
	"last_name" VARCHAR(100) NOT NULL,
	"date_of_birth" DATE,
	"address" TEXT,
	"phone" VARCHAR(20),
	"citizenship_id" VARCHAR(50) UNIQUE,
	"ssn_hash" VARCHAR(100),
	"is_active" BOOLEAN DEFAULT true,
	PRIMARY KEY("holder_id")
);




CREATE TABLE IF NOT EXISTS "Account" (
	"account_id" UUID DEFAULT gen_random_uuid(),
	"account_number" VARCHAR(20) NOT NULL UNIQUE,
	"holder_id" UUID NOT NULL,
	"account_type" VARCHAR(50) NOT NULL,
	"balance" NUMERIC(15,2) NOT NULL DEFAULT 0.00,
	"interest_rate" NUMERIC(5,4) DEFAULT 0.00,
	"status" VARCHAR(20) NOT NULL DEFAULT 'Active',
	"created_at" TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	PRIMARY KEY("account_id")
);


CREATE INDEX "idx_account_holder"
ON "Account" ("holder_id");

CREATE TABLE IF NOT EXISTS "Transaction" (
	"transaction_id" BIGSERIAL,
	"account_id" UUID NOT NULL,
	"transaction_type" VARCHAR(50) NOT NULL,
	"amount" NUMERIC(15,2) NOT NULL,
	"timestamp" TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	"description" TEXT,
	"related_account_number" VARCHAR(20),
	"status" VARCHAR(20) NOT NULL DEFAULT 'Completed',
	PRIMARY KEY("transaction_id")
);


CREATE INDEX "idx_transaction_account"
ON "Transaction" ("account_id");

CREATE TABLE IF NOT EXISTS "Loan" (
	"loan_id" UUID DEFAULT gen_random_uuid(),
	"holder_id" UUID NOT NULL,
	"loan_type" VARCHAR(50) NOT NULL,
	"principal_amount" NUMERIC(15,2) NOT NULL,
	"current_balance" NUMERIC(15,2) NOT NULL,
	"interest_rate" NUMERIC(5,4) NOT NULL,
	"term_months" INTEGER NOT NULL,
	"start_date" DATE NOT NULL,
	"status" VARCHAR(20) NOT NULL DEFAULT 'Pending',
	PRIMARY KEY("loan_id")
);




CREATE TABLE IF NOT EXISTS "LoanPayment" (
	"payment_id" BIGSERIAL,
	"loan_id" UUID NOT NULL,
	"payment_date" DATE NOT NULL,
	"amount_paid" NUMERIC(15,2) NOT NULL,
	"principal_paid" NUMERIC(15,2) NOT NULL,
	"interest_paid" NUMERIC(15,2) NOT NULL,
	PRIMARY KEY("payment_id")
);




CREATE TABLE IF NOT EXISTS "Card" (
	"card_id" UUID DEFAULT gen_random_uuid(),
	"account_id" UUID NOT NULL,
	"card_number" VARCHAR(16) NOT NULL UNIQUE,
	"card_type" VARCHAR(20) NOT NULL,
	"expiration_date" DATE NOT NULL,
	"cvv_hash" VARCHAR(100),
	"daily_limit" NUMERIC(10,2) DEFAULT 500.00,
	"status" VARCHAR(20) NOT NULL DEFAULT 'Active',
	"issue_date" DATE DEFAULT CURRENT_DATE,
	PRIMARY KEY("card_id")
);




CREATE TABLE IF NOT EXISTS "ChequeBookRequest" (
	"request_id" BIGSERIAL,
	"account_id" UUID NOT NULL,
	"request_date" TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	"number_of_leaves" INTEGER NOT NULL DEFAULT 50,
	"delivery_address" TEXT,
	"status" VARCHAR(20) NOT NULL DEFAULT 'Pending',
	PRIMARY KEY("request_id")
);




CREATE TABLE IF NOT EXISTS "TermsOfService" (
	"term_id" SERIAL,
	"version_number" VARCHAR(20) NOT NULL UNIQUE,
	"content" TEXT NOT NULL,
	"effective_date" DATE NOT NULL UNIQUE,
	PRIMARY KEY("term_id")
);



ALTER TABLE "User"
ADD FOREIGN KEY("role_id") REFERENCES "Role"("role_id")
ON UPDATE NO ACTION ON DELETE RESTRICT;
ALTER TABLE "AccountHolder"
ADD FOREIGN KEY("user_id") REFERENCES "User"("user_id")
ON UPDATE NO ACTION ON DELETE CASCADE;
ALTER TABLE "Account"
ADD FOREIGN KEY("holder_id") REFERENCES "AccountHolder"("holder_id")
ON UPDATE NO ACTION ON DELETE RESTRICT;
ALTER TABLE "Transaction"
ADD FOREIGN KEY("account_id") REFERENCES "Account"("account_id")
ON UPDATE NO ACTION ON DELETE RESTRICT;
ALTER TABLE "Loan"
ADD FOREIGN KEY("holder_id") REFERENCES "AccountHolder"("holder_id")
ON UPDATE NO ACTION ON DELETE RESTRICT;
ALTER TABLE "LoanPayment"
ADD FOREIGN KEY("loan_id") REFERENCES "Loan"("loan_id")
ON UPDATE NO ACTION ON DELETE CASCADE;
ALTER TABLE "Card"
ADD FOREIGN KEY("account_id") REFERENCES "Account"("account_id")
ON UPDATE NO ACTION ON DELETE RESTRICT;
ALTER TABLE "ChequeBookRequest"
ADD FOREIGN KEY("account_id") REFERENCES "Account"("account_id")
ON UPDATE NO ACTION ON DELETE RESTRICT;