#include <CISConsole.h>
#include <mbed.h>

#include "ADCSObjects.h"
#include "Configuration.h"
#include "events/Events.h"
#include "interfaces/CDH.h"

/**
 * @brief Initializes the all of the subclasses of the ADCS
 *
 * @return mbed_error_status_t
 */
mbed_error_status_t initialize() {
  LOG("Init", "Initialization starting");
  mbed_error_status_t result = MBED_SUCCESS;

  LOG("Init", "Initialization complete");
  return result;
}

/**
 * @brief Exectutes the main loop of the ADCS
 *
 * @return mbed_error_status_t error code
 */
mbed_error_status_t run() {
  uint32_t now               = HAL_GetTick();
  uint32_t nextControlLoop   = now + PERIOD_MS_CONTROL_LOOP;
  uint32_t nextPeriodicEvent = now + PERIOD_MS_PERIODIC;

  mbed_error_status_t result = MBED_SUCCESS;
  while (true) {
    now = HAL_GetTick();
    if (now >= nextControlLoop && (nextControlLoop >= PERIOD_MS_CONTROL_LOOP ||
                                      now <= PERIOD_MS_CONTROL_LOOP)) {
      result = eventControlLoop();
      if (result) {
        ERROR("Run", "Failed to perform control loop event: 0x%02X", result);
        return result;
      }
    } else if (now >= nextPeriodicEvent &&
               (nextPeriodicEvent >= PERIOD_MS_PERIODIC ||
                   now <= PERIOD_MS_PERIODIC)) {
      result = eventPeriodic();
      if (result) {
        ERROR("Run", "Failed to perform periodic event: 0x%02X", result);
        return result;
      }
      nextPeriodicEvent = now + PERIOD_MS_PERIODIC;
    } else if (cdh.hasMessage()) {
      result = cdh.processMessage();
      if (result) {
        ERROR("Run", "Failed to process message from the bus: 0x%02X", result);
        return result;
      }
    } else {
      wait_ms(PERIOD_MS_IDLE_SLEEP);
    }
  }
}

/**
 * Program start routine
 * @return error code
 */
int main(void) {
  mbed_error_status_t result = initialize();
  if (result) {
    ERROR("ADCS", "Failed to initialize: 0x%02X", result);
  }
  result = run();
  if (result) {
    ERROR("ADCS", "Failed to run: 0x%02X", result);
  }
  return MBED_SUCCESS;
}
