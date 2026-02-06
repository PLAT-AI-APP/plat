# JPA Entity 설계 가이드

<BaseEntity_패턴>
@com/plat/platdata/entity/BaseEntity.java
- LocalDateTime createdAt : Entity 생성날짜
- LocalDateTime updatedAt : Entity 변경날짜
</BaseEntity_패턴>

<연관관계_매핑_원칙>
## 1. ID 네이밍 규칙
``` java
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id") // 컬럼명은 이곳에 명시
    private Long id; // "id" 로 작성하는 것이 관례
```

## 2. 관계 매핑 전략
- **JPA 양방향 관계** : 부모-자식 라이프사이클이 동일할 때 (cascade ALL, orphanRemoval)
  - 예: User ↔ Authentication, Character ↔ CharacterLanguagePack
- **JPA 단방향 @OneToMany** : 부모가 자식을 소유하지만 자식에서 부모 탐색이 불필요할 때
  - 예: Lorebook → LorebookTranslation, Scenario → ScenarioTranslation
- **plain FK (Long 컬럼)** : 독립적 라이프사이클 또는 서비스 계층에서 삭제를 제어할 때
  - 예: ChatRoom.userId, Credit.userId, Payment.subscriptionId

## 3. cascade 삭제
- `repository.delete(entity)` 호출 시 JPA cascade로 연관 엔티티가 함께 삭제된다.
- 하단 cascade 관계에 명시된 엔티티들이 연쇄 삭제 대상이다.

## 4. 하단 연관관계 표기 규칙
1. 0,1 : 0개, 1개만 가능
2. 0,N : 0개, N개만 가능
3. 1,N : 반드시 1개 이상
4. 1 : 반드시 1개
5. N : 반드시 N개

예시) User(1) : Authentication(1,N)
= User는 반드시 1개, Authentication는 반드시 1개 이상의 관계
</연관관계_매핑_원칙>

<연관관계_설계>

## User

### User cascade 관계
1. User 삭제
2. Authentication, Token, Verification 삭제
3. Credit, CreditTransaction, CreditBalance, Payment, Subscription 삭제
4. Creator, Persona 삭제
5. ChatRoom 삭제
6. Character SoftDelete 요청

### Authentication
- OAuth2, 이메일로그인
- User(1) : Authentication(1,N) 관계,
- 멀티로그인 연동

### Token
- JWT 토큰
- 멀티 플랫폼 지원
- User(1) : Token(1,N) 관계

### Verification
- 인증정보(성인인증여부, 휴대폰인증여부, 이메일인증여부 등)
- User(1) : Verification(0,N) 관계

---

## Credit
- 크레딧은 AI 캐릭터와 대화할때 사용되는 토큰
- 모든 토큰은 유효기간(현재 1년)이 존재
- 크레딧 충전 후 7일 이내 환불가능 (따라서 지급액과 잔여액이 컬럼에 존재)

### Credit
- 만료, 잔액 등 담당
- User(1) : Credit(0,N)
- Credit(1) : Payment(0,1), Payment == null 이면 결제를 통한 획득이 아님
- 크레딧은 구매(Payment)뿐만이 아니라 이벤트나 다른 획득경로가 존재

### CreditTransaction
- 크레딧 기록용, 증거용
- User(1) : CreditTransaction(0,N)
- 입출금내역 담당

### CreditBalance
- 크레딧 빠른 조회용
- User(1) : CreditBalance(1)
- 데이터 정합성, 동시성 이슈가 관건

---

## Payment
- 결제방식, 결제일 등 실제 결제에 대한 정보를 담당
- User(1) : Payment(0,N)

## Subscription
- 구독형 BM
- User(1) : Subscription(0,N)

---
## Character
- 유저에 의해 만들어진 AI 페르소나 캐릭터
- 국제화가 관건이기 때문에 언어팩 구현
- 유저가 Character를 만들기위해서는 반드시 Creator가 되어야한다.
- **유저가 탈퇴(삭제)되어도 캐릭터는 삭제되어선 안된다.**

### Character cascade 관계
- SoftDelete 방식채택
  - 1명이라도 채팅중이라면 : 삭제불가, 비공개만 가능
  - 삭제요청 - 즉시 비공개처리, 7일 이후에 운영자 계정으로 캐릭터 귀속, 채팅방이 없다면 안전하게 삭제처리
- Character 삭제 시 cascade
- CharacterTranslation, CharacterLanguagePack 삭제
- Scenario, Lorebook, Tag 삭제

### Creator
- Character 는 Creator가 만들수있다.
- Creator가 되기 위해서는 휴대폰인증(Verification) 정보가 있어야 한다.
- User(1) : Creator(0,1)

### Character(CharacterEntity)
- 예약어로 인해 CharacterEntity 사용
- Character는 언어에 국한되지 않은 모든 캐릭터 데이터가 존재
- Creator(1) : Character(0,N)
- User 삭제에 의한 cascade로 Creator가 삭제되면 관리자계정으로 귀속된다.(비공개상태로 자동 전환)

### CharacterLanguagePack (국제화 허브)
- 하나의 언어로 묶기 위한 매핑
- Character(1) : CharacterLanguagePack(1,N)
- 언어(Language) 하나로 모든 데이터를 가져올 수 있는게 특징이다.
- 데이터 하나라도 다른 언어가 포함될 수 없기 때문에 언어팩으로 묶음
- **허브 역할**: 언어별 모든 번역 데이터가 이 엔티티를 통해 연결됨
  - CharacterLanguagePack(1) : CharacterTranslation(1)
  - CharacterLanguagePack(1) : ScenarioTranslation(0,N)
  - CharacterLanguagePack(1) : LorebookTranslation(0,N)
  - CharacterLanguagePack(1) : Tag(1,N)

### CharacterTranslation
- 하나의 언어로 구성된 캐릭터 설정정보
- CharacterLanguagePack(1) : CharacterTranslation(1)

---

## Scenario
- 캐릭터와의 대화에서 시나리오를 구성

### Scenario cascade 관계
- Scenario 삭제 시 cascade
- ScenarioTranslation 삭제

### Scenario
- 여러개의 언어로 구성되어있기 때문에 ScenarioTranslation과 분리되어있다.
- Character(1) : Scenario(1,N)

### ScenarioTranslation
- 하나의 언어로 구성된 시나리오 정보
- Scenario(1) : ScenarioTranslation(1,N)
- CharacterLanguagePack(1) : ScenarioTranslation(0,N)

---

## Lorebook
- 캐릭터와의 대화에서 새로운 이벤트를 담당

### Lorebook cascade 관계
- Lorebook 삭제 시 cascade
- LorebookTranslation 삭제 (keywords @ElementCollection 포함)

### Lorebook
- 여러개의 언어로 구성되어있기 때문에 LorebookTranslation과 분리되어있다.
- Character(1) : Lorebook(1,N)

### LorebookTranslation
- 하나의 언어로 구성된 이벤트 정보
- Lorebook(1) : LorebookTranslation(0,N)
- CharacterLanguagePack(1) : LorebookTranslation(0,N)
- keywords: @ElementCollection (lorebook_translation_keyword 테이블)

---

## Hashtag
- 해시태그는 캐릭터의 특징을 나타내는 지표이다.
- 임의생성이 아닌 운영자에 의해 생성되는 시스템태그 개념이다.
- 즉, 정해진 틀안에서 선택되어야 한다.

### Hashtag cascade 관계
- 운영자에 의해 관리되기 때문에 삭제될 가능성 매우 낮음
- Hashtag 삭제 불가

### Hashtag
- 태그코드만 담고있다. 이는 언어에 국한되지 않기 위함이다.

### HashtagTranslation
- 하나의 언어로 구성된 해시태그, tagName은 실제 표시되는 태그명이다.
- Hashtag(1) : HashtagTranslation(1,N)

### Tag
- 캐릭터에 붙은 태그
- HashTag(1) : Tag(0,N)
- CharacterLanguagePack(1) : Tag(1, N)
- Tag는 언어팩과 해시태그와의 연관관계를 만들기 위해 생성되었다.
- TODO : 구조에 대해서는 조금더 고민해봐야겠다.
  - 예시) Locale.KO 에서 "히어로물" 태그로 검색
  - "히어로물" 태그가 붙은 캐릭터를 불러옴(언어팩:KO)
  - 불러온 데이터는 모두 Language가 KO이어야함

---

## Chat
- 채팅은 Persona와 Character가 대화하는 공간이며, Language가 적용되있어야 한다.
- 언어 선택 우선순위
  - 사용자 선호 언어(User.Language)
  - 웹페이지 사용자 설정언어(웹에서 언어 전환)
  - 브라우저 언어(Accept-Language)
- 위의 우선순위에 따라 ChatRoom.Language가 결정되어야 한다.

### Chat cascade 관계
- ChatRoom 삭제 시 cascade
- Memory, Message 삭제

### ChatRoom
- 채팅관련 메타데이터
- User(1) : ChatRoom(0,N)
- Character(1) : ChatRoom(0,N)
- Persona(1) : ChatRoom(0,N)
- Scenario(1) : ChatRoom(0,N)
- 페르소나는 사용자 선택에 의해 변경될 수 있다.
- 페르소나는 Persona.isDefault == true 인 페르소나가 최초 선택된다.

``` markdown
User가 Character와 대화하기 위해 '대화시작'을 눌러 채팅방에 입장 -> 아직 ChatRoom 생성 X
Character - Character Language Pack - Scenario_Translation 데이터만 단순 출력

- 상황1) 사용자가 대화하지 않고 나감 : 아무런 변화 없음 (ChatRoom 생성기록 없음)
- 상황2) 사용자가 대화를걸어 채팅시작:
    - ChatRoom 생성
    - Message에 AI의 첫대사와 사용자 답장이 생성됨
    - 왜 AI의 첫대사를 Message에 넣나? : Creator가 첫대사를 변경하면 그 영향을 받기 때문임
따라서 ChatRoom은 반드시 1개 이상의 Message를 가지고있음
```

### Persona
- User의 멀티프로필
- User는 여러개의 페르소나를 가지고있고, 그 페르소나를 통해 AI 캐릭터와 대화할 수 있다.
- User(1) : Persona(1,N)
- User 별로 여러개의 페르소나중 isDefault가 true인 컬럼은 단 하나여야하고, 삭제할 수 없다.
- 삭제하기위해서는 기본페르소나를 다른 페르소나로 변경하고 삭제하여야 한다.

### Message
- 사용자와 AI 캐릭터의 메세지 데이터
- ChatRoom(1) : Message(1,N)

### Memory
- 사용자와 AI 캐릭터의 대화를 압축저장
- turnCheckpoint에 의해 압축할 Message 양을 결정
- 1 ~ 1005개의 Message가 존재하고, Memory.turnCheckpoint = 10이라고 가정한다면
  - Memory.turnCheckpoint 를 보고 10,20,30,...,1000 데이터 컬럼을 조회
  - 최대 사이즈만큼 substring (예를들어 1만자라고 한다면 최근데이터부터 1만자까지)
  - 프롬프트에 Memory 부분에 적재
- TODO : 사용자에 의해 중간 채팅데이터를 수정할수있게 했을때 이 장기기억 데이터를 어떻게 관리해야할지 논의해야함
  - 중간 995번째 메시지를 수정했다면? -> 장기기억 메모리를 어떻게 수정해야됨?
- ChatRoom(1) : Memory(0,N)


</연관관계_설계>

