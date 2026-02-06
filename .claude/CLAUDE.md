# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Build & Run Commands

```bash
./gradlew build                              # Full build (all modules)
./gradlew :plat-data:test                    # Run plat-data tests only
./gradlew :plat-ai:test                      # Run plat-ai tests only
./gradlew :plat-boot:bootRun                 # Run the boot application
./gradlew :plat-data:test --tests "com.plat.platdata.SomeTest"  # Single test
```

## Architecture

Spring Boot 4.0.2 multi-module Gradle project (Java 25, Gradle 9.3) for an AI chatbot platform with persona-driven conversations. Uses Spring AI 2.0.0-M2 for multi-provider LLM support.

### Module Dependency Graph

```
plat-ai  →  plat-data
plat-boot →  plat-data
```

- **plat-data** — JPA entity and repository layer. No AI dependencies. Contains all domain entities, enums, and Spring Data JPA repositories.
- **plat-ai** — AI orchestration layer. Depends on plat-data. Handles chat model routing (Claude, Gemini, GPT), prompt construction, conversation history, and streaming responses via `Flux<String>`.
- **plat-boot** — Bootable application that combines plat-data. Component-scans both `com.plat.platboot` and `com.plat.platdata`.

### plat-data Entity Structure (`com.plat.platdata.entity`)

All entities extend `BaseEntity` (`@MappedSuperclass` with `createdAt`/`updatedAt` audit fields). Standard patterns: `@Getter`, `@NoArgsConstructor(access = PROTECTED)`, `@Builder`, `GenerationType.IDENTITY`.

Domain groups under `entity/`:
- **user/** — User, Creator, Authentication, Verification, Token
- **character/** — CharacterEntity, CharacterLanguagePack, CharacterTranslation, Hashtag, Lorebook, LorebookTranslation, Scenario, ScenarioTranslation
- **chat/** — Persona, ChatRoom, Message, Memory
- **credit/** — Credit, CreditBalance, CreditTransaction, Payment, Subscription

Repositories follow `{Entity}Repository extends JpaRepository<Entity, Long>` in `com.plat.platdata.repository`.

### plat-ai AI Client Pattern (`com.plat.platai.ai`)

Strategy pattern for multi-provider support:
1. `ChatModelClient` interface defines `stream()`/`call()` contract
2. `AbstractChatModelClient` base implementation wraps Spring AI's `ChatClient`
3. Provider packages (`claude/`, `gemini/`, `gpt/`) contain concrete clients + `@Configuration` for bean wiring
4. `ChatModelClientHandler` routes by `ChatModel` enum to the correct client

`PersonaPromptBuilder` constructs a 4-layer system prompt: Platform Rules → Character Setting → Long-term Memory → Conversation History.

### Database

H2 in-memory database with `ddl-auto: create-drop` for development/testing. Test config in `plat-data/src/test/resources/application.yaml`.

### Supported AI Models

- **Claude**: CLAUDE_HAIKU_4_5, CLAUDE_SONNET_4_5 (via `spring-ai-starter-model-anthropic`)
- **Gemini**: GEMINI_2_5_FLASH, GEMINI_2_5_PRO, GEMINI_3_0_FLASH, GEMINI_3_0_PRO (via `spring-ai-starter-model-google-genai`)
- **GPT**: GPT_5_1 (via `spring-ai-starter-model-openai`)

All models use temperature 0.7. Streaming via Project Reactor `Flux<String>`.

### i18n

Korean (default), English, Japanese. Error messages in `errors*.properties` resolved via `MessageSource` and Accept-Language header.