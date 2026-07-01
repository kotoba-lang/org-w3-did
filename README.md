# did

`kotoba-lang/did` is a small EDN-first DID library.

It keeps DID identifiers and DID Documents as ordinary Clojure data. The first
scope is parsing, deterministic document construction, and local resolution for
`did:key` Ed25519 keys and `did:web` documents.

## Usage

```clojure
(require '[did.core :as did])

(did/parse "did:web:example.com:users:alice#keys-1")
;; => {:did "did:web:example.com:users:alice"
;;     :method "web"
;;     :method-id "example.com:users:alice"
;;     :fragment "keys-1"}

(did/did-web-document "example.com" {:path ["users" "alice"]})
;; => {:@context ["https://www.w3.org/ns/did/v1"]
;;     :id "did:web:example.com:users:alice"
;;     :verificationMethod [] ...}
```

## Scope

- DID / DID URL parsing
- DID Document EDN construction
- `did:key` Ed25519 document from multicodec 0xed01 public keys
- `did:web` id / well-known URL helpers

Out of scope for this first cut: remote HTTP resolution, signing, verification,
JSON-LD expansion, and key derivation. Those belong in crypto-specific libraries
such as `kotoba-lang/ed25519`.

## Test

```bash
clojure -M:test
```

## License

MIT
