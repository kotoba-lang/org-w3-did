(ns did.core-test
  (:require [clojure.test :refer [deftest is]]
            [did.core :as did]))

(deftest parse-did-url
  (is (= {:did "did:web:example.com:users:alice"
          :method "web"
          :method-id "example.com:users:alice"
          :fragment "keys-1"}
         (did/parse "did:web:example.com:users:alice#keys-1")))
  (is (= {:did "did:key:z6Mkabc"
          :method "key"
          :method-id "z6Mkabc"
          :path "path"
          :query "service=agent"
          :fragment "frag"}
         (did/parse "did:key:z6Mkabc/path?service=agent#frag")))
  (is (thrown? clojure.lang.ExceptionInfo (did/parse "not-a-did"))))

(deftest did-key-roundtrip
  (let [pub (vec (range 32))
        id (did/public-key->did-key pub)]
    (is (did/did-key-ed25519? id))
    (is (= pub (did/did-key->public-key id)))
    (let [doc (did/did-key-document id)
          kid (str id "#" (subs id (count "did:key:")))]
      (is (= id (:id doc)))
      (is (= kid (-> doc :verificationMethod first :id)))
      (is (= [kid] (:authentication doc))))))

(deftest did-web-helpers
  (is (= "did:web:example.com:users:alice"
         (did/did-web "example.com" "users" "alice")))
  (is (= "https://example.com/.well-known/did.json"
         (did/did-web-url "did:web:example.com")))
  (is (= "https://example.com/users/alice/did.json"
         (did/did-web-url "did:web:example.com:users:alice")))
  (let [doc (did/did-web-document "example.com" {:path ["users" "alice"]})]
    (is (= "did:web:example.com:users:alice" (:id doc)))
    (is (= [did/did-context] (get doc did/context-key)))))

(deftest local-resolution
  (let [id (did/public-key->did-key (repeat 32 1))]
    (is (= id (:id (did/resolve-local id)))))
  (is (= "did:web:example.com"
         (:id (did/resolve-local "did:web:example.com")))))
