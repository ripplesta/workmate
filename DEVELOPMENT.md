# WorkMate学習記録

## 1週目(7/21~7/27)  

###  Spting Bootを使ったプロジェクトの初作成
- まず初めに動作確認をしたがファイルを作成しようとしてエラーが発生  
    →　Servletとjspを使用するものだと思っていたが.javaとHTMLで作成するのだと理解した
- 実行時にエラー発生(DB接続の設定をしていなくてエラー)
    →application propertiesにDB接続情報を記述して解決
- サンプルファイルを作成してデータの受け渡しも問題なく実行できた

  #### 学びメモ
  - `GetMapping("/hello")`で/helloにアクセスするとそのメソッドが実行される  
      → `return "hello"`;で`template/hello.html`を表示する

### 「ログイン機能」実装記録
- 入力フォームからログインIDとパスワードを受け取る  
- Spring Bootの `@PostMapping` で受け取りDBと照合  
- 一致した場合はセッション情報を作成し、メニューページへ遷移  
- 一致しない場合は、エラーメッセージを赤い文字で表示させる

  #### 学びメモ
  - `@アノテーション` により、クラスやメソッドに役割や制約を指定できる
  - `@Entity`はJavaクラスをDBテーブルに対応させる。`@Table`でテーブル名をマッピングしたり`@Column`でエンティティを定義して自動生成し   たりできる
  - `@Id`で主キー指定や`@GenerationType.IDENTITY`で主キー値を自動で割り当てる
  - `Model`はコントローラーからビューへ値を渡すための入れ物(Mapのようなもの)
  - `addAttribute("キー", 値)`で文字列やオブジェクトなどを渡せる
  - html側は`"${キー}"`で受け取る  
    (例　`model.addAttribute("title", "ログインページ")`
      → `<h1 th:text="${title}</h1>` <!-- ログインページ -->
  - `"*{キー}"`はフォーム用でth:objectで指定されたオブジェクトを基準にしてアクセスできる  
    (例　コントローラー側`addAttribute("loginForm",new LoginForm())`
      → HTML側`<form th:action="@{/login}" th:object="${loginForm}"`
     `<input type="text" th:field="*{loginId}" />`  
    これで`*{loginId}` ← `taskForm.getLoginId()`にバインドされる)  
      → 実際には`<input name="text" value="${loginForm.loginId}" />`と同じ
   - 補足：name属性・value属性・id属性(自動で生成される)を自動でセットしてくれる便利なもので  
     `<input type="text" th:field="*{loginId}" />`
     これは実際には
     `<input type="text" name="loginId" id="loginId" value="(loginForm.lodinIdの値)" />`となる  
  - `@ModelAttribute`を付与するとフォームなどから入力されたデータを指定したオブジェクトに保存してコントローラーで処理できる
  - `HttpSession` でセッション管理ができる(例：`session.setAttribute("loginUser", account)`でセッションに保存
  - `session.getAttribute("loginUser")`でセッションから値を取得
      → `Account loginUser = (Account)session.getAttribute("loginUser")`など型変換が必要
  - HTML側で使うときは`th:text="${session.loginUser.userName}"`と`session.`を使って属性にアクセス
  - `session.invalidate()`でセッション破棄


    ##### リポジトリの`extends JpaRepository<Account, Long>`の説明
    - Spring Data JPA を使ってデータベースアクセスを行う 
    - `Account`は対象となるエンティティ(テーブル)
    - `Long{はそのエンティティの主キー(@Idで指定された)の型  
    これを継承することで  
      - `findAll()` → 全件取得
      - `findById(id)` → IDで1件取得
      - `save(entity)` → 新規登録 or 更新
      - `delete(entity)` → 削除
      - `count()` → レコード数の取得  
    が使用できるようになる  
    - このインターフェースで`Account`テーブルに対して基本的なCRUD操作が使えるようになる(実装不要)  
        →　自動でSQLが作られ、ログインID検索なども簡単に書ける  
    ##### `findByLoginId(String loginId)`
    - Spting Data JPAのメソッド名でクエリ生成という仕組みを使ってloginIdで検索する処理を自動生成する  
         → SELECT * FROM account WHERE login_id = ?　を自動でやっている  


### 「新規ユーザー登録」実装記録
- 新規登録ページから入力された情報を受け取る
- `@PostMapping`で受け取りDBに同じ値があればログインIDは使用されていますと返す
- 同じ値がなければDBに保存して完了する

#### 学びメモ
- `findByLoginId(registerForm.getLoginId())`でDBからログインIDをみて`.isPresent()`で同じ値があるか確認し、あったらログインIDは使用されているとエラーを返す
- なければインスタンスを作成し受け取った情報を格納していく。
- `accountRepository.save(newUser)`で格納されているデータをDBに保存

## 2週目(7/28~8/3) 

### タスク管理機能実装記録
- ユーザーごとにタスクを表示
- 登録や編集・削除ボタンで管理
- 登録や編集などを押すとフォーム画面に遷移して入力する
- 編集機能に手間取ってこの週に実装できなかった

  #### 学びメモ
  - とりあえず最初はログインしているユーザーごとにタスクを表示させるようにした
  - エンティティに`@ManyToOne`、`@JoinColumn(name = "user_id")`でアカウントテーブルの`user_id`を外部キーにする
  - セッションに保存されている情報を取得して`findByUser(loginUser)`でユーザーIDが一致しているタスクの情報をDBから持ってきてhtml側に渡し表示する
  - 1つのコントローラーに登録、編集、削除の処理を実装した
  - 登録時にフォームから送られてきたデータをDBに保存する時userIdだけは現在のセッション情報から持ってくる
  - フォームは同じものを`form th:action=`をつかって登録か編集かを判断して表示させることもできる  
      → `form th:action="${taskForm.id == null ? @{(/tasks/create)} : @{(/tasks/edit/update)}"`でidに何も入ってなければ登録、入っていれば編集になる  
   他の記述もth:ifで分岐させてどっちかを表示されるなどが可能
  - `RequestMapping("/tasks")`で全体の共通パスになる
    `@GetMapping("/new")`などにすると/tasks/newとなる
  - `Repository`はJPAの内部機能によって決められた命名規則でメソッド名を書くと自動生成してくれる
    例 `findByLoginId(String loginId)`で渡されたログインIDを検索  
    `findByUser(Account user)`とすると`@ManyToOne`で外部キーを参照しているので自動でuser_idに変換して検索  
  - `return "redirect:/`でそのページにリダイレクト  
    redirectはhtmlではなくブラウザに指示. 
    例えば`redirect:/tasks/new`なら/tasks/newというURLにGETアクセスする  
    SpringはそのURLに対応する`@GetMapping("/tasks/new")`を探す  

## 3週目(8/4~8/10)

### タスク機能実装記録
- 編集機能の続きを実装
- タスクの検索やソート機能

  #### 学びメモ
  - フォームから渡された情報をDBに保存するとき一緒にログインしているUserIdを保存したいのでセッション情報を取得して`task.setUser(account)`などで一緒に保存
  - `th:href="@{'/tasks/edit/' + ${task.id}`という形にするとtasks/edit/1などタスクのIDの情報を含めて編集フォームに遷移できる  
      → Get /edit/{id} でマッピングして`(@PathVariable Long id)`でURLに含まれる動的パラメータを受け取りリポジトリでidをDBから探して編集フォームに渡す  
  → POST /tasks/updateなどで編集したい情報をタスクIDと共にデータを送ってDBに更新処理をする(タスクIDを送らないと新規作成になってしまう)
  - 削除を押すとタスクのIDを`@PathVariable`で受け取って`findById()`でDBからタスクを探し`teskRepository.delete()`でそれを削除する
  - Spring Securityを実装したがわからないことだらけだったのでファイルに詳細にコメントでメモをした

### ソート・検索機能実装記録
- 簡単な検索やソート機能
- 検索はタイトルにキーワードで検索して絞る
- ソートは単純に期限日に対して昇順にする

  #### 学習メモ
  - リンクにsortBy=""を埋め込みRequestParamで受け取る
  - `List<Task> tasks = taskRepository.findAll(Sort.by(sortBy).ascending())`でsortByによって呼び出された属性にascendingで昇順にして返す
  - 検索はリポジトリに`findByTitleContaining(String keyword)`などを用意しておき、フォームアクションでKeywordを受け取って絞られたタスクを返す

### Spring Securityの導入
- アクセス制限を付与(ログインしていないとアクセスできない)
- 新規登録時にパスワードをBCryptで暗号化　ログイン時にもパスワードをBCryptを使って認証
- ログアウトの処理(セッション情報の破棄、認証情報のクリアなど)

  #### 学習メモ
  - 今後ログイン情報を取得したいときは
    `@AuthenticationPrincipal AccountUserDetails userDetails`
    `Account user = userDetails.getAccount()`などで取得できる
  - SecurityConfigはセキュリティ設定クラスで、URLごとにアクセス制限やログイン関連の細かい設定、パスワード比較時のエンコード方法の設定などができる
  - `requestMatchers("/login")`などで/loginにアクセスするときは制限がない(ログインが必要ない)などの設定ができる
  - `PasswordEncoder`でBCryptなどでハッシュ化して保存、認証に使用する
  - `formLogin()`を使用するとデフォルトで`username`,`password`というフィールドを使用するのでデフォルトの名前と変えているなら`.usernameParameter()`や`.passwordParameter()`でフォームのname属性指定しないと認証がうまくいかなくなってしまうので注意
  - `DaoAuthenticationProvider`に`UserDetailsService`を登録する(情報取得サービスを使用する)
  - `UserDetailsServiceはUserDetails`を実装してログイン時に入力されたログインIDからDBのユーザ情報を探すクラス
  - ログイン時に自動で`loadUserByUsername(String loginId)`を呼び出しDBからユーザー情報を探し見つけたらUserDetailsオブジェクトに変換して返す
  - UserDetailsはUserDetailsインターフェースを実装したクラスで、Spring Securityが必要とするユーザー情報(ID、パスワード、権限など)をまとめる

## 4週目(8/11~8/17)

### ソート機能+検索機能の複合実装記録
- もう少し複雑であったら便利のような難しいものに挑戦してみたくて実装した
- タイトルやカテゴリなど特定の中でキーワードで検索して絞れる
- 作成日や期限日を昇順や降順にできる
- 上記の機能を複合して使うことも可能

  #### 学習メモ
  - `Specification`は`JPA Criteria API`を裏で使っていて、ラムダの引数`(root, query, cb)`の cbがそれにあたる
  - (root, query, cb) ->  
    - root:エンティティのメタ情報(Taskのカラムにアクセスする)  
    - query:実行中のクエリ全体(select句とかorder byとか)  
    - cb:CriteriaBuilder(where句に必要な条件を組み立てる道具箱)  
  - `JpaSpecificationExecutor`をリポジトリに追加すると柔軟な動的検索ができる
  - CriteriaBuilderが持っている標準メソッドが色々あり便利
  - 検索条件を組み立てるためのクラスTaskSpecificationsを作成し、  
    `public static Specification<Task> titleContains(String title) {
        return (root, query, cb) ->  
            (title == null || title.isEmpty()) ? null :
            cb.like(cb.lower(root.get("title")), "%" + title.toLowerCase() + "%");
    }`　など検索条件のメソッドを記述する  
  - フォームアクションで検索・ソートしたい情報をRequestParamで受け取る
  - `Specification<Task> spec = Specification.where(TaskSpecifications.userIdEquals(loginUser.getUserId()))
												.and(TaskSpecifications.titleContains(title))`…  
    と組み合わせの情報を詰めていく  
  - `Sort sort = order.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending()`  
    でソートの方向の情報を受け取り`List<Task> tasks = taskRepository.findAll(spec, sort)`で受け取った情報で検索・ソートをして実行した結果のタスクを返していく
  - HtML側は`th:value`やselectで選択式にして`<option value="仕事" th:selected="${category=='仕事'}">仕事</option>`で情報を送る

### チャットボット風機能実装記録
- チャットボットページを作成し、そこでやりとりができる
- メッセージをユーザーとボットで区別して やりとりしたメッセージをDBに保存し、履歴も見れる
- まずは単純にルールベースをキーワードで反応するようにした
- キーワードに対応した定型文(例えばおはようならおはようございます！と返す)などをDBでテーブルで用意しておき、メッセージに対して定型文で返す
- 現状はメッセージを送った後は同じ画面に遷移させる(更新処理)
- チャットページの見た目を見やすくした

  #### 学習メモ
  - エンティティに`@Enumerated(EnumType.STRING)
	private SenderType senderType;`を用意してUSERかBOTかを区別する
  - メッセージをDBに保存する処理を@PostMappingでメッセージが送られたときに実行する(しっかりuserIdの情報も送らないとユーザーごとにメッセージが表示されなくなってしまう)
  - `th:each="msg : ${messages}" class="message" th:classappend="${msg.senderType.name().toLowerCase()}"`でメッセージがユーザーなのかボットなのか判断し、cssなどでコーティングすると見た目がわかりやすい
  - リポジトリでfindByKeywordContainingIgnoreCase(userInput)などで部分一致で探し、見つけたらリストにしてその定型文を入れる、見つからなかったらデフォルトの定型文を返す
 
## 5週目(8/18~8/24)
### 動作確認＋細かい修正
- 見た目を少し整えた
- 動作の確認をした

  #### 学習メモ
  - 動作確認をするため少し動かしていたらログアウト周りがうまくいっておらず、セッションが破棄されていなかった
    `.invalidateHttpSession(true)`
	`.clearAuthentication(true)`を追加し.logoutSuccessUrl`("/logout-success")`に直したら`NoResourceFoundException`がでるようになった
	色々試して中々特定できなかったが`.logoutUrl("/logout")`がGETでやっているものだと勘違いしていてログアウトをPOSTで送ったらエラーが解消した
  - ログイン画面や新規登録画面をcssを用いて少しだけ整えた

### チャットボット風機能実装記録
- ページにアクセスしたときやメッセージをしたときに自動で下にスクロールされるようにした
- チャットの返答の仕組みに機能を追加
- ユーザーのメッセージに含まれるキーワードで複数候補がでたときに優先度によってランダムで返すのを実装
- メッセージを時間帯によって区別して、その時間帯にあった返答をできるようにした
- 返答の定型文にタグを付与し、メッセージに含まれるキーワードからタグにあった返答を候補に選ぶように実装予定
  - タグはDBを用意し、中間テーブルもつくって定型文とタグのIDを紐づけした
  

  #### 学習メモ
  - HTML側でJSを利用して  
    `function scrollToBottom() {
    	const container = document.getElementById("messages");  
    	container.scrollTop = container.scrollHeight;  
    }`と記述することで下にいくようにした  
  - 定型文に優先度を数字でつけて重みづけをし、特定のキーワードに複数の定型文があったときにランダムで返すようにした  
    (例　3つ候補があり10、5、5と優先度がついていると50%で1つ目の候補を返す、25%で2つ目の候補を返すなど)  
  - `int totalWeight = responses.stream().mapToInt(BotResponse::getPriority).sum();`で全ての候補から優先度の値を合計し
    `int randomValue = new Random().nextInt(totalWeight);`で合計の値からランダムに数字をだし
    繰り返しで`current += res.getPriority()`していって`if(randomValue < current)`でcurrentがランダムに出した数字を超えたら  
	return res.getTemplateText();で繰り返し時点の定型文を返す。なので優先度が高いほどその定型文がでやすくなる仕組み
  - 現在の時間帯を渡し`if(now.isAfter(LocalTime.of(5, 0)) && now.isBefore(LocalTime.of(12, 0)))`としてこの条件に当てはまるなら朝
    `if(now.isBefore(LocalTime.of(18, 0)))`この条件に当てはまるなら昼などとした
  - リストの候補を渡し`responses.stream().filter(r -> r.getTimeRange() == TimeRange.ANY || r.getTimeRange() == currentTime).collect(Collectors.toList())`で時間帯が一致するかそれ以外(ANY)のものだけで絞り当てはまるリストを返す
  - DBにタグ用のテーブルをつくり`@ManyToMany(mappedBy = "tags")`でSetを用意
  - 同じくボットの返答側にも@ManyToManyを用意し
	`@JoinTable(`
			`name = "bot_response_tag",`
			`joinColumns = @JoinColumn(name = "bot_response_id"),`
			`inverseJoinColumns = @JoinColumn(name = "tag_id"))`で中間テーブルも用意して紐づけた


## 6週目(8/25~8/31)
### チャットボット風機能実装記録
- 引き続き機能を追加していく
- メッセージに含まれるキーワードからタグにあった返答を候補に選ぶようにした
- チャットでコマンド形式でタスクの管理をできるようにした
  - タスクの登録とリストの表示を実装した
  - リストの表示は検索とソートを組み合わせて使うこともできる

  #### 学習メモ
  - `Map.entry("おはよう", "挨拶")`などのMapを用意しておき、`if(userInput.contains(entry.getKey()))` 
		`detectedTags.add(entry.getValue())`でメッセージにKeyが含まれていればValueをタグとして渡す
  - 渡したタグを候補の数繰り返しで見ていき`if(tags.contains(tag.getName()))`で定型文にそのタグが含まれていれば `.add(res)`でそのタスクを加える(breakをいれると複数にならなくて済む)
　- `チャットでコマンドを認識するためにCommandのDTOとCommandParserクラスを用意
  - フィールドを用意するときにメッセージの引数を渡して処理を実行するコンストラクタを作成
    - .split(" /")で空白＋/で分割させたものを配列で用意
    - 最初の/〇〇でコマンドを認識(例：/list→ list)
    - その次のコマンドからは/xxx 〇〇 という形になるので2つずつ抽出してKey, ValueとしてMapに追加していく(例：/優先度 高 など)
  - その後`getOptions("Key")`で要求されたワードを検索したりタスク登録につかったりする
  - コマンドかどうかは`if(command.getAction().equals("list"))`などで判断してtrueだったらその処理を行う
 
## 7週目(9/1~9/7)
### チャットコマンド実装記録
- チャットコマンドの種類を増やしていく
- 既存のタスクの編集機能を再利用して`/update`でタスクの編集を実装
  - 同じく編集機能を利用して`/done` タスク番号で完了`/doing`で進行中`/todo`で未着手に上書きできる
- チャットコマンドが少し入力が違うだけで受け付けないので表記ゆれなどにある程度対応させた
  - 日本語と英語どっちにも対応したり、/タイトルや/タスク名などでも反応する
- /helpでコマンドの説明や使い方などを表示できる
  - /help /コマンド名でさらにそのコマンドの詳細を表示
- /statsで全体の進捗やカテゴリ別の進捗がみれる
  - /stats /今日や/週間でその範囲で進捗を見ることも可

  #### メモ
  - CommandAliasクラスを作成してMapを用意し`Map.entry("タイトル","title")`などをいれて表記ゆれに対応させる
    - `Map.of`でもできるが10個以上記述するとエラーになるので`Map.entry`と使い分ける
    - メソッドを作成し`return ACTION_ALIASES.getOrDefault(key, key)`のようにして返す
    - CommandParserに`String nomalizedKey = CommandAlias.normalizeField(value[0].trim())`
	  `options.put(nomalizedKey, value[1].trim());`のように通すと表記ゆれがある程度直せる
  - 全体の進捗の集計にはMap<String, Map<String, Integer>>のようにMapの中にさらにMapを用意してfor文で進捗を完了・進行中・未着手でカウントしたものをカテゴリ別にさらにカウントさせる

## 8週目(9/8~9/15)
### タスク管理実装記録
- タスクの検索に新たに期限日を指定できるようにした
- 日付の期間指定や1週間・1ヶ月単位で検索することもできる
  - 1週間や1ヶ月はボタンを押すことによって簡単に検索できる
- カレンダーをテーブルなどを用いて自分で作成し、そこにタスクも表示させ実装
