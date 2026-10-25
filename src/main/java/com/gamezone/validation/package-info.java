/**
 * Validation layer: one validator per functional module. Validators are
 * invoked by the services, throw the custom exceptions and never call the
 * services or the repositories.
 */
package com.gamezone.validation;
