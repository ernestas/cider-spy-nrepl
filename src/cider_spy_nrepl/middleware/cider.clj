(ns cider-spy-nrepl.middleware.cider
  (:require [clojure.tools.nrepl.transport :as transport]
            [clojure.tools.nrepl.misc :refer [response-for]]
            [cider-spy-nrepl.middleware.summary-builder :as summary-builder]
            [cider-spy-nrepl.middleware.sessions :as sessions]
            [cheshire.core :as json]))

(defn ^:dynamic send-back-to-cider! [transport session-id message-id s]
  (transport/send transport
                  (response-for {:session session-id :id message-id} :value s)))

(defn update-spy-buffer-summary!
  "Send this string back to the users CIDER SPY buffer.
   EMACS CIDER SPY has a listener waiting for a message with an ID
   the same as SUMMARY-MESSAGE-ID in the session."
  [session]
  (let [summary (summary-builder/summary @session)
        {:keys [id summary-message-id transport]} @session]
    (send-back-to-cider! transport id summary-message-id (json/encode summary))))

(defn update-session-for-summary-msg!
  "Update the session with SUMMARY-MESSAGE-ID."
  [session {:keys [id]}]
  (sessions/update! session assoc :summary-message-id id))

(defn send-connected-msg!
  "Send a message back to CIDER-SPY pertaining to CIDER-SPY-HUB connectivity.
   The correct ID is used as to ensure the message shows up in the relevant
   CIDER-SPY buffer."
  [session s]
  (let [{:keys [id hub-connection-buffer-id transport]} @session]
    (transport/send transport (response-for {:session id :id hub-connection-buffer-id}
                                            :value (str "CIDER-SPY-NREPL: " s)))))
